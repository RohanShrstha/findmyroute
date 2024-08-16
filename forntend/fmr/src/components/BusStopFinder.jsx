import { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import {MapContainer, TileLayer, Marker, Popup} from "react-leaflet";
import "leaflet/dist/leaflet.css";
import Swal from "sweetalert2";
import RoutingHighlight from "./RoutingHighlight.jsx";
import axios from "axios";
import Header from "./Header/Header.jsx";
const BusStopFinder = () => {
    const [busStopData, setBusStopData] = useState([]);
    const [isDataLoading, setIsDataLoading] = useState(true);

    const [isRouteLoading, setIsRouteLoading] = useState(true);
    const [isLocationSet, setIsLocationSet] = useState(true);
    const [error, setError] = useState(null);

    const [routesForHighLights, setRoutesForHighLights] = useState([]);

    const [locationFetched, setLocationFetched] = useState(false);

    const [markerPosition, setMarkerPosition] = useState([]);

    const [locationSearchValue,setLocationSearchValue] = useState([]);

    const [nearestToDestination, setNearestToDestination] = useState([]);
    const [nearestToUser, setNearestToUser] = useState([]);

    const NEPAL_LATITUDE_BOUNDS = { min: 26.3475, max: 30.4474 };
    const NEPAL_LONGITUDE_BOUNDS = { min: 80.0586, max: 88.2015 };

    const colors = ['green', 'blue', 'red', 'yellow', 'purple'];


    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setMarkerPosition([position.coords.latitude, position.coords.longitude])
                    setLocationFetched(true);
                },
                (error) => {
                    setError(error.message);
                    setLocationFetched(true);
                }
            );
        } else {
            console.log('Geolocation is not supported by this browser.');
            setLocationFetched(false);
        }
    }, []);


    useEffect(() => {
        if (locationFetched) {
            const fetchBusStopDataMethod = async () => {
                try {
                    const response = await axios.get("http://localhost:8091/bus-stop");
                    setBusStopData(response.data.data);
                    return response;
                } catch (error) {
                    setError(error.message);
                } finally {
                    setIsDataLoading(false);
                }
            };

            fetchBusStopDataMethod().then(()=>{
            });
        }
    }, [locationFetched]);

    const searchLocation = (query) => {
        const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)},nepal&format=json`;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                if (data.length > 0) {
                    setLocationSearchValue([])
                    setLocationSearchValue((data));
                } else {
                    console.log("Location not found");
                }
            })
            .catch(error => {
                console.error("Error fetching data:", error);
            });
    }

    const searchInput = (event)=>{
        const value = event.target.value;

        setTimeout(() => {
            searchLocation(value);
        }, 3000);
    }

    const onLocationSelect = (data)=>{
        const fetchRouteData = async () => {
            setIsRouteLoading(true);
            try {
                const requestData = {
                    "userLat": markerPosition[0],
                    "userLng": markerPosition[1],
                    "destinationLat": data.lat,
                    "destinationLng": data.lon,
                }
                const response = await axios.post("http://localhost:8091/find-route",requestData );
                return response;
            } catch (error) {
                setError(error.message);
            } finally {
                setIsRouteLoading(false);
            }
        };

        fetchRouteData().then(r => {
            const data = r.data.data;
            const routings = data.routing;
            let routingsLength = routings.length;
            const arr = [];
            if(routingsLength > 0){
                routings.map(x=>{
                    arr.push([x.ltd,x.lng])
                })
            }
            setRoutesForHighLights(arr);
            setNearestToDestination([data.nearestToDestination.ltd,data.nearestToDestination.lng]);
            setNearestToUser([data.nearestToUser.ltd,data.nearestToUser.lng])
            setLocationSearchValue([]);
            setIsLocationSet(false);
        });
    }

    if(!locationFetched) {
        return <div>Unable to fetch the current location</div>;
    }

    if(isDataLoading && isRouteLoading && isLocationSet) {
        return (
            <div className="ringcolor">
                <div className="ring">
                    Loading
                    <div className="ringspin"></div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div style={{width: "100vw", height: "100vh", padding: "10vw 30vw"}}>
                <div className="d-flex justify-content-center">
                    <img src={"src/assets/error.png"} style={{height: "100px", width: "100px"}}/>
                </div>
                <div className="d-flex justify-content-center mt-3">Error</div>
                <div className="d-flex justify-content-center mt-3">{error}</div>
            </div>
        );
    }

    const selectLoc = (index)=>{
        const data = locationSearchValue[index];
        onLocationSelect(data);
    }

    return (
        <>
            <Header onInputChange={searchInput} />
                        {locationSearchValue.length > 0 && (
                            <ul className="dropdown-menu show" style={{position: "absolute", right: "10px", width: "35%" }}>
                                {locationSearchValue.map((result, index) => (
                                    <li key={index} className="dropdown-item" onClick={() => selectLoc(index)}>
                                        {result.display_name.length > 30 
                                ? `${result.display_name.substring(0, 60)}...`
                                : result.display_name
                            }
                                    </li>
                                ))}
                            </ul>
                        )}
            <div className="w-full">
                <div className="map text-muted">
                    <MapContainer
                        center={markerPosition}
                        zoom={14.5}
                        style={{
                            height: "100vh",
                        }}
                    >
                        <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"/>

                        {/* User marker */}
                        <Marker
                            position={markerPosition}
                            draggable={false}
                        />

                        {
                            busStopData.map((stop, index) => (
                                <Marker
                                    key={index}
                                    position={[
                                        parseFloat(stop.ltd),
                                        parseFloat(stop.lng),
                                    ]}
                                    icon={L.icon({
                                        iconUrl: "src/assets/bus-stop.png", // Replace 'path/to/red-marker-icon.png' with the path to your red marker icon
                                        iconSize: [18, 30],
                                        iconAnchor: [12, 41],
                                        popupAnchor: [10, -35],
                                    })}
                                >
                                    <Popup>
                                        <div
                                            className=""
                                            style={{lineHeight: "2px", fontSize: "12px"}}
                                        >
                                            <h5>
                                                <b>{stop.name}</b>
                                            </h5>
                                        </div>
                                    </Popup>
                                </Marker>
                            ))
                        }

                        {nearestToUser.length >= 2 && (
                            <div>
                                {/* main route */}
                                <RoutingHighlight from={markerPosition} to={nearestToUser} color={colors[0]} isMain={false}/>
                            </div>
                        )}

                        {
                            routesForHighLights.length > 0 && (
                                <div>
                                    <RoutingHighlight color={colors[2]} isMain={true} routes={routesForHighLights}/>
                                </div>
                            )
                        }

                        {nearestToDestination.length >= 2 && (
                            <div>
                                {/* main route */}
                                <RoutingHighlight from={nearestToUser} to={nearestToDestination} color={colors[1]} isMain={true} busNo={3}/>
                            </div>
                        )}

                        <div
                            className="lonlat"
                            style={{
                                position: "relative",
                                padding: "0 5px",
                                marginTop: "98vh",
                                zIndex: "1000",
                                background: "white",
                                color: "black",
                            }}
                        >
                            Lat : {markerPosition[0]} , Lon : {markerPosition[1]}
                        </div>
                    </MapContainer>
                </div>
            </div>
        </>
    );
};

export default BusStopFinder;
