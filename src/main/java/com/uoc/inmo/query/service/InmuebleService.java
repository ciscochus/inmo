package com.uoc.inmo.query.service;

import java.util.List;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

public interface InmuebleService {
    
    public List<InmuebleSummary> fetchInmuebleSummary(int offset, int limit);

    public int getInmuebleSummaryCount();
}
