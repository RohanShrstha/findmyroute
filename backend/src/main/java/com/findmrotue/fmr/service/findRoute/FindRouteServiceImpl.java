package com.findmrotue.fmr.service.findRoute;

import com.findmrotue.fmr.dto.BusStopDTO;
import com.findmrotue.fmr.dto.FindRouteDTO;
import com.findmrotue.fmr.dto.RoutesDTO;
import com.findmrotue.fmr.entity.bustop.BusStop;
import com.findmrotue.fmr.entity.routes.Routes;
import com.findmrotue.fmr.service.busstop.BusStopService;
import com.findmrotue.fmr.service.routes.RoutesService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.findmrotue.fmr.algorithm.HaversineUtils.calculateHaversineDistance;

@Service
public class FindRouteServiceImpl implements FindRouteService{
    private final RoutesService routesService;
    private final BusStopService busStopService;

    public FindRouteServiceImpl(RoutesService routesService, BusStopService busStopService) {
        this.routesService = routesService;
        this.busStopService = busStopService;
    }

//    @Override
//    public RoutesDTO findRouteAndNearestBusStop(FindRouteDTO dto) {
//        Map<String, String> map = new HashMap<>();
//        List<BusStop> busStopList = busStopService.getAll();
//
//        BusStopDTO nearestBusToDestination = new BusStopDTO();
//        BusStopDTO nearestBusToUser = new BusStopDTO();
//
//        List<BusStopDTO> busStopDTOListDestination = getNearestBusstop(dto, busStopList, 0);
//        List<BusStopDTO> mutableBusStopDTOListDestination = new ArrayList<>(busStopDTOListDestination);
//        mutableBusStopDTOListDestination.sort(Comparator.comparing(BusStopDTO::getDistance));
//
//        Optional<BusStopDTO> closestDTODestination = busStopDTOListDestination.stream()
//                .sorted(Comparator.comparing(BusStopDTO::getDistance))
//                .limit(3)
//                .min(Comparator.comparing(d -> calculateHaversineDistance(
//                        Double.parseDouble(dto.getDestinationLat()),
//                        Double.parseDouble(dto.getDestinationLng()),
//                        Double.parseDouble(d.getLtd()),
//                        Double.parseDouble(d.getLng())
//                )));
//
////        if(closestDTO0.isPresent()){
////            nearestBusToDestination = closestDTO0.get();
////        }
////        else{
////            nearestBusToDestination = mutableBusStopDTOList0.get(0);
////
////        }
//        nearestBusToDestination = mutableBusStopDTOListDestination.get(0);
//
//
//        List<BusStopDTO> busStopDTOListUser = getNearestBusstop(dto, busStopList, 1);
//        List<BusStopDTO> mutableBusStopDTOListuser = new ArrayList<>(busStopDTOListUser);
//        mutableBusStopDTOListuser.sort(Comparator.comparing(BusStopDTO::getDistance));
//
//        Optional<BusStopDTO> closestDTOUser = busStopDTOListUser.stream()
//                .sorted(Comparator.comparing(BusStopDTO::getDistance))
//                .limit(2)
//                .min(Comparator.comparing(d -> calculateHaversineDistance(
//                        Double.parseDouble(dto.getDestinationLat()),
//                        Double.parseDouble(dto.getDestinationLng()),
//                        Double.parseDouble(d.getLtd()),
//                        Double.parseDouble(d.getLng())
//                )));
//
////        if(closestDTOUser.isPresent()){
////            nearestBusToUser = closestDTOUser.get();
////        }
////        else{
////            nearestBusToUser = mutableBusStopDTOListuser.get(0);
////        }
//
//        nearestBusToUser = mutableBusStopDTOListuser.get(0);
//
//
//        return new RoutesDTO(nearestBusToUser, nearestBusToDestination);
//    }

    public RoutesDTO findRouteAndNearestBusStop(FindRouteDTO dto) {
        Map<String, String> map = new HashMap<>();
        List<BusStop> busStopList = busStopService.getAll();
        List<BusStopDTO> busStopDTOList = getNearestBusstop(dto, busStopList, 0);
        List<BusStopDTO> mutableBusStopDTOList = new ArrayList<>(busStopDTOList);
        mutableBusStopDTOList.sort(Comparator.comparing(BusStopDTO::getDistance));
        BusStopDTO nearestBusToDestination = new BusStopDTO();
        BusStopDTO nearestBusToUser = new BusStopDTO();
        Routes nearestRoute = new Routes();
        if(!busStopDTOList.isEmpty()){
            nearestBusToDestination = mutableBusStopDTOList.get(0);
            List<Routes> routesList = this.routesService.getByBusStopId(nearestBusToDestination.getId());
            routesList.stream().forEach(routes -> {
                List<BusStopDTO> nearestBusstop = getNearestBusstop(dto, routes.getRouting(), 1);
                List<BusStopDTO> mutableBusStopDTOListForNearestBusStop = new ArrayList<>(nearestBusstop);
                mutableBusStopDTOListForNearestBusStop.sort(Comparator.comparing(BusStopDTO::getDistance));
                if(!nearestBusstop.isEmpty()){
                    map.put(routes.getId().toString(),mutableBusStopDTOListForNearestBusStop.get(0).getId().toString());
                }
            });

            Optional<Map.Entry<String, String>> smallestEntry = map.entrySet().stream()
                    .min((entry1, entry2) -> {
                        double value1 = Double.parseDouble(entry1.getValue());
                        double value2 = Double.parseDouble(entry2.getValue());
                        return Double.compare(value1, value2);
                    });

            if (smallestEntry.isPresent()) {
                nearestRoute = this.routesService.getById(Integer.parseInt(smallestEntry.get().getKey()));
                nearestBusToUser = BusStopDTO.toDto(this.busStopService.getById(Integer.parseInt(smallestEntry.get().getValue())));
            }

        }
        return new RoutesDTO(nearestRoute.getId(), nearestRoute.getRouting(),nearestBusToUser, nearestBusToDestination);
    }

    private static List<BusStopDTO> getNearestBusstop(FindRouteDTO dto, List<BusStop> busStopList, int x) {
        if(x == 0){
            return busStopList.stream().map(busStop -> {
                double busStopLatitude = Double.parseDouble(busStop.getLtd().replace(",", "."));
                double busStopLongitude = Double.parseDouble(busStop.getLng().replace(",", "."));
                double distance = calculateHaversineDistance(Double.parseDouble(dto.getDestinationLat()), Double.parseDouble(dto.getDestinationLng()), busStopLatitude, busStopLongitude);
                return new BusStopDTO(busStop.getId(), busStop.getName(), busStop.getLng(), busStop.getLtd(), distance);
            }).toList();
        }
        else{
            return busStopList.stream().map(busStop -> {
                double busStopLatitude = Double.parseDouble(busStop.getLtd().replace(",", "."));
                double busStopLongitude = Double.parseDouble(busStop.getLng().replace(",", "."));
                double distance = calculateHaversineDistance(Double.parseDouble(dto.getUserLat()), Double.parseDouble(dto.getUserLng()), busStopLatitude, busStopLongitude);
                return new BusStopDTO(busStop.getId(), busStop.getName(), busStop.getLng(), busStop.getLtd(), distance);
            }).toList();
        }
    }
}
