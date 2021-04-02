package com.uoc.inmo.query;

import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountInmuebleSummariesQuery {
    InmuebleFilter filter;
}
