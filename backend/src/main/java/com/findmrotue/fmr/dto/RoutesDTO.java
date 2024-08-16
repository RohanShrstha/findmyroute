package com.findmrotue.fmr.dto;

import com.findmrotue.fmr.entity.bustop.BusStop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutesDTO {
    private Integer id;
    private List<BusStop> routing;
    private BusStopDTO nearestToUser;
    private BusStopDTO nearestToDestination;

    public RoutesDTO(BusStopDTO nearestBusToUser, BusStopDTO nearestBusToDestination) {
        this.nearestToUser = nearestBusToUser;
        this.nearestToDestination = nearestBusToDestination;
    }
}
