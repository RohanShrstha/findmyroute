package com.findmrotue.fmr.controller.findRoute;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findmrotue.fmr.abstracts.BaseController;
import com.findmrotue.fmr.config.CustomMessageSource;
import com.findmrotue.fmr.dto.FindRouteDTO;
import com.findmrotue.fmr.dto.RoutesDTO;
import com.findmrotue.fmr.entity.routes.Routes;
import com.findmrotue.fmr.service.findRoute.FindRouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/find-route")
public class FindRouteController extends BaseController {
    private final FindRouteService service;
    private final CustomMessageSource customMessageSource;
    private final ObjectMapper mapper;

    public FindRouteController(FindRouteService service, CustomMessageSource customMessageSource, ObjectMapper mapper) {
        this.service = service;
        this.customMessageSource = customMessageSource;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> findRoute(@RequestBody FindRouteDTO dto) throws IOException {
        RoutesDTO navigationRotues = service.findRouteAndNearestBusStop(dto);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.get", customMessageSource.get("findroute")),navigationRotues)
        );
    }

}

