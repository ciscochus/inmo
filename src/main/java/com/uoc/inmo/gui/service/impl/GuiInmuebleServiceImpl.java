package com.uoc.inmo.gui.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.query.api.response.ResponseFile;
import com.uoc.inmo.query.api.response.ResponsePrice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    @Value("${service.url.getImage}")
    private String getImageUrl;

    @Value("${service.url.getInmueblePriceHistory}")
    private String getInmueblePriceHistoryUrl;

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

    @Override
    public ResponseFile getImage(UUID idImage) {
        
        if(idImage == null)
            return null;

        try {
            ResponseEntity<ResponseFile> response = restTemplate.getForEntity(getImageUrl+"/"+idImage.toString(), ResponseFile.class);
            if(response.hasBody())
                return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ResponsePrice> getInmueblePriceHistory(UUID inmuebleId) {
        
        if(inmuebleId == null)
            return null;

        // RestTemplate restTemplate = new RestTemplate();
        
        // MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    
            
        // converter.setSupportedMediaTypes(
        //             Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM}));
    
        // restTemplate.setMessageConverters(Arrays.asList(converter, new FormHttpMessageConverter()));

        try {
            ResponseEntity<ResponsePrice[]> response = restTemplate.getForEntity(getInmueblePriceHistoryUrl+"/"+inmuebleId.toString(), ResponsePrice[].class);
            if(response.hasBody())
                return Arrays.asList(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public RequestInmueble updateInmueble(RequestInmueble request) {
        try {
            restTemplate.put(createInmuebleUrl, request);

        } catch (Exception e) {
            return null;
        }

        return request;
    } 
    
}
