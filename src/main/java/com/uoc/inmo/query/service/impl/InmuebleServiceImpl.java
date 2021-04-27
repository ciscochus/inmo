package com.uoc.inmo.query.service.impl;

import java.util.List;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmuebleSummaryRepository;
import com.uoc.inmo.query.service.InmuebleService;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InmuebleServiceImpl implements InmuebleService{
    
    @NonNull
    private final InmuebleSummaryRepository inmuebleSummaryRepository;

    @Override
    public List<InmuebleSummary> fetchInmuebleSummary(int offset, int limit){
        return inmuebleSummaryRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    @Override
    public int getInmuebleSummaryCount() {
        return Long.valueOf(inmuebleSummaryRepository.count()).intValue();
    }
}
