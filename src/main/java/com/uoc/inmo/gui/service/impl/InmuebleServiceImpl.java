package com.uoc.inmo.gui.service.impl;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.service.InmuebleService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InmuebleServiceImpl implements InmuebleService {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${service.url.createInmueble}")
    private String createInmuebleUrl;

    @Override
    public void createInmueble(RequestInmueble request) {
        System.out.println(createInmuebleUrl);
        
    }
    
}
