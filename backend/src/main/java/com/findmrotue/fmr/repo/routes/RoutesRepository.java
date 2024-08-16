package com.findmrotue.fmr.repo.routes;

import com.findmrotue.fmr.entity.routes.Routes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoutesRepository extends JpaRepository<Routes, Integer> {
    @Query("SELECT r FROM Routes r JOIN r.routing bs WHERE bs.id = :busStopId")
    Optional<List<Routes>> findByBusStopId(@Param("busStopId") Integer busStopId);

}
