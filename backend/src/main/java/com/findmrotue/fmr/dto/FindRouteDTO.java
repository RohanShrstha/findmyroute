package com.findmrotue.fmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindRouteDTO {
    private String userLat;
    private String userLng;
    private String destinationLat;
    private String destinationLng;
}
