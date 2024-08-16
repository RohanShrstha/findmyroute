package com.findmrotue.fmr.service.routes;

import com.findmrotue.fmr.entity.routes.Routes;
import com.findmrotue.fmr.repo.routes.RoutesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RouterServiceImpl implements RoutesService{

    private final RoutesRepository repo;

    public RouterServiceImpl(RoutesRepository repo) {
        this.repo = repo;
    }

    @Override
    public Routes create(Routes dto) {
        return repo.save(dto);
    }

    @Override
    public List<Routes> getAll() {
        return repo.findAll();
    }

    @Override
    public Routes getById(Integer id) {
        return repo.findById(id).get();
    }

    @Override
    public Routes update(Routes dto) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<Routes> getByBusStopId(Integer id) {
        Optional<List<Routes>> byBusStopId = repo.findByBusStopId(id);
        if(byBusStopId.isPresent()){
            return byBusStopId.get();
        } else {
            return new ArrayList<Routes>();
        }
    }
}
