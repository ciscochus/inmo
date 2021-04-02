package com.uoc.inmo.query.entity.inmueble;

import lombok.Data;

@Data
public class CountInmuebleSummariesResponse {
    Integer count;
    Long lastEvent;
    public CountInmuebleSummariesResponse(Integer count, Long lastEvent) {
        this.count = count;
        this.lastEvent = lastEvent;
    }

    
}
