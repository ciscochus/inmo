package com.uoc.inmo.query.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.query.api.response.ResponseFile;
import com.uoc.inmo.query.entity.inmueble.InmuebleImages;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmuebleImagesRepository;
import com.uoc.inmo.query.repository.InmuebleSummaryRepository;
import com.uoc.inmo.query.service.InmuebleService;
import com.uoc.inmo.query.utils.ConvertionUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InmuebleServiceImpl implements InmuebleService{
    
    @NonNull
    private final InmuebleSummaryRepository inmuebleSummaryRepository;

    @NonNull
    private final InmuebleImagesRepository inmuebleImagesRepository;

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

    @Override
    public ResponseFile getImage(UUID id) {
        Optional<InmuebleImages> entity = inmuebleImagesRepository.findById(id);

        if(entity.isPresent())
            return convertToResponse(entity.get());

        return null;
    }

    @Override
    public List<ResponseFile> getInmuebleImages(UUID id) {
        List<InmuebleImages> entities = inmuebleImagesRepository.findByInmuebleId(id);

        List<ResponseFile> responseList = new ArrayList<>();

        for (InmuebleImages image : entities) {
            ResponseFile imageResponse = convertToResponse(image);
            if(imageResponse != null)
                responseList.add(imageResponse);
        }

        return responseList;
    }


    private ResponseFile convertToResponse(InmuebleImages source){
        if(source == null)
            return null;

        UUID id = source.getId();
        String name = source.getName();
        String mimeType = source.getMimeType();
        String base64Content = ConvertionUtils.toString(source.getContent());

        if(!StringUtils.hasText(base64Content))
            return null;

        String content = new String(Base64.getDecoder().decode(base64Content));

        return new ResponseFile(id, name, mimeType, content);
    }
}
