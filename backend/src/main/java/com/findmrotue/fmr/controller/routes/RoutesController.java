package com.findmrotue.fmr.controller.routes;

import com.findmrotue.fmr.abstracts.BaseController;
import com.findmrotue.fmr.entity.routes.Routes;
import com.findmrotue.fmr.service.routes.RoutesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/routes")
public class RoutesController extends BaseController {
    private final RoutesService service;

    public RoutesController(RoutesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Routes routes) throws IOException {
        Routes res = service.create(routes);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.create", customMessageSource.get("message")),res)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Routes> dtos = service.getAll();
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.get.all", customMessageSource.get("message")),dtos)
        );
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id){
        Routes dto = service.getById(id);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.get", customMessageSource.get("message")),dto)
        );
    }

    @GetMapping("/bus-stop-id/{id}")
    public ResponseEntity<?> getByBusStopId(@PathVariable("id") Integer id){
        List<Routes> dto = service.getByBusStopId(id);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.get", customMessageSource.get("message")),dto)
        );
    }


    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Integer id){
        service.deleteById(id);
        return ResponseEntity.ok(
                successResponse(customMessageSource.get(
                        "crud.delete", customMessageSource.get("message")),null)
        );
    }
}
