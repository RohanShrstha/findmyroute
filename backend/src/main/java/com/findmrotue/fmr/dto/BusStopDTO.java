package com.findmrotue.fmr.dto;

import com.findmrotue.fmr.entity.bustop.BusStop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusStopDTO {
    private Integer id;
    private String name;
    private String lng;
    private String ltd;
    private Double distance;

    public static BusStopDTO toDto(BusStop entity){
        return new BusStopDTO(entity.getId(), entity.getName(), entity.getLng(), entity.getLtd(), 0.0);
    }
}
