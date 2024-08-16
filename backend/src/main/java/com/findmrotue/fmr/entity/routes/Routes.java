package com.findmrotue.fmr.entity.routes;

import com.findmrotue.fmr.entity.bustop.BusStop;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Routes {
    @Id
    @SequenceGenerator(name = "routes_sequence", sequenceName = "routes_sequence", allocationSize = 1)
    @GeneratedValue(generator = "routes_sequence", strategy = GenerationType.SEQUENCE)
    private Integer id;
    @ManyToMany
    @JoinTable(
            name = "route_bus_stop",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "bus_stop_id")
    )
    private List<BusStop> routing;
}
