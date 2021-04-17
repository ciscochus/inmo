package com.uoc.inmo.gui.data.service;

import java.util.List;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmuebleSummaryRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InmuebleService {
    
    @NonNull
    private final InmuebleSummaryRepository inmuebleSummaryRepository;

    public List<InmuebleSummary> fetchInmuebleSummary(int offset, int limit){
        return inmuebleSummaryRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public int getInmuebleSummaryCount() {
        return Long.valueOf(inmuebleSummaryRepository.count()).intValue();
    }
}
