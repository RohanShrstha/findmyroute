package com.findmrotue.fmr.service.routes;


import com.findmrotue.fmr.entity.routes.Routes;

import java.util.List;

public interface RoutesService {
    public Routes create(Routes dto);

    public List<Routes> getAll();

    public Routes getById(Integer id);

    public Routes update(Routes dto);

    public void deleteById(Integer id);

    public List<Routes> getByBusStopId(Integer id);
}
