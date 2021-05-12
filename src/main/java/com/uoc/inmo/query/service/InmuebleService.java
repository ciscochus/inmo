package com.uoc.inmo.query.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

public interface InmuebleService {
    
    public List<InmuebleSummary> fetchInmuebleSummary(int offset, int limit, Optional<InmuebleSummaryFilter> filter);

    public int getInmuebleSummaryCount(Optional<InmuebleSummaryFilter> filter);

    public InmuebleSummary getInmuebleSummaryById(UUID id);
}
