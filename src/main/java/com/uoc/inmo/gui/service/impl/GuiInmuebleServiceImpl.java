package com.uoc.inmo.gui.service.impl;

import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.service.GuiInmuebleService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuiInmuebleServiceImpl implements GuiInmuebleService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${service.url.createInmueble}")
    private String createInmuebleUrl;

    @Value("${service.url.deleteInmueble}")
    private String deleteInmuebleUrl;

    @Override
    public RequestInmueble createInmueble(RequestInmueble request) {
        
        ResponseEntity<String> response = restTemplate.postForEntity(createInmuebleUrl, request, String.class);

        if(response.hasBody() && response.getStatusCode().equals(HttpStatus.OK)){
            return request;
        }
        
        return null;
    }

    @Override
    public boolean deleteInmueble(UUID idInmueble) {
        
        if(idInmueble == null)
            return false;

        try {
            restTemplate.delete(deleteInmuebleUrl+"/"+idInmueble.toString());

        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
}
