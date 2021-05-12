package com.uoc.inmo.query.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
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
    public List<InmuebleSummary> fetchInmuebleSummary(int offset, int limit, Optional<InmuebleSummaryFilter> filter){

        if(filter.isPresent())
            return inmuebleSummaryRepository.findAll(filter.get().getSpecification(), PageRequest.of(offset, limit)).getContent();

        return inmuebleSummaryRepository.findAll(PageRequest.of(offset, limit)).getContent(); 
    }

    @Override
    public int getInmuebleSummaryCount(Optional<InmuebleSummaryFilter> filter) {
        if(filter.isPresent())
            return Long.valueOf(inmuebleSummaryRepository.count(filter.get().getSpecification())).intValue();

        return Long.valueOf(inmuebleSummaryRepository.count()).intValue();
    }

    @Override
    public InmuebleSummary getInmuebleSummaryById(UUID id){
        Optional<InmuebleSummary> entity = inmuebleSummaryRepository.findById(id);

        if(entity.isPresent())
            return entity.get();

        return null;
    }

}
