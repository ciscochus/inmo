package com.uoc.inmo.query;

import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;

import lombok.Value;

@Value
public class FetchInmuebleSummariesQuery {
    int offset;
    int limit;
    InmuebleFilter filter;
}
