import { useEffect } from 'react';
import { useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet-routing-machine';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import 'leaflet-control-geocoder';

const RoutingHighlight = ({ from, to, color, isMain = false, routes = [], busNo = 2 }) => {
    const map = useMap();

    useEffect(() => {
        if (!map) return;

        let points = [];
        if (routes.length > 0) {
            routes.forEach(r => {
                points.push(L.latLng(r[0], r[1]));
            });
            points.push(L.latLng(routes[0][0], routes[0][1]));
        } else {
            points = [
                L.latLng(from[0], from[1]),
                L.latLng(to[0], to[1])
            ];
        }

        const routingControl = L.Routing.control({
            waypoints: points,
            routeWhileDragging: false,
            geocoder: L.Control.Geocoder.nominatim(),
            lineOptions: {
                styles: [{ color: color, weight: 5 }]
            },
            createMarker: function() { return null; },  // To hide markers if not needed
            show: !isMain,
        }).addTo(map);

        if (isMain) {
            routingControl.on('routesfound', (e) => {
                const routeCoordinates = e.routes[0].coordinates;
                createBusMarkers(busNo, routeCoordinates);
            });
        }

        const createBusMarkers = (numberOfBuses, coordinates) => {
            const busIcons = [];

            // Create bus icons dynamically
            for (let j = 0; j < numberOfBuses; j++) {
                const busIcon = L.icon({
                    iconUrl: `src/assets/bus6.png`,
                    iconSize: [25, 25],
                });
                busIcons.push(busIcon);
            }

            // Function to animate a bus marker
            const animateBus = (index, coordinates, delay) => {
                // Pick a random starting point from the route coordinates
                const randomIndex = Math.floor(Math.random() * (coordinates.length - 1));
                const busMarker = L.marker([coordinates[randomIndex].lat, coordinates[randomIndex].lng], { icon: busIcons[index] }).addTo(map);

                const updateBusPosition = (i) => {
                    if (i < coordinates.length) {
                        busMarker.setLatLng([coordinates[i].lat, coordinates[i].lng]);
                        setTimeout(() => updateBusPosition(i + 1), delay); // Recursive call with delay
                    } else {
                        // Remove the bus marker once it reaches the destination
                        map.removeLayer(busMarker);
                        // Add a new bus marker after the current one is removed
                        // setTimeout(() => animateBus(index, coordinates, delay), delay);
                        // Re-add the bus marker to the start of the route
                        animateBus(index, coordinates, delay);
                    }
                };

                updateBusPosition(randomIndex + 1); // Start animation after marker creation from the next point
            };

            // Create bus markers at random positions along the route
            for (let j = 0; j < numberOfBuses; j++) {
                const randomDelay = Math.floor(Math.random() * 5000) + 5000; // Random delay between 100ms and 5s
                animateBus(j, coordinates, randomDelay);
            }
        };

        return () => {
            if (routingControl && map) {
                map.removeControl(routingControl);
            }
        };
    }, [map, from, to, color, isMain]);

    return null;
};

export default RoutingHighlight;
