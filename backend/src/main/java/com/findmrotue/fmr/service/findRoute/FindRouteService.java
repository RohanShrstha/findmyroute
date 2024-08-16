package com.findmrotue.fmr.service.findRoute;

import com.findmrotue.fmr.dto.FindRouteDTO;
import com.findmrotue.fmr.dto.RoutesDTO;
import com.findmrotue.fmr.entity.routes.Routes;

public interface FindRouteService {

    RoutesDTO findRouteAndNearestBusStop(FindRouteDTO dto);
}
