package com.uoc.inmo.gui.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.command.api.request.RequestInmuebleSubscription;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.query.api.response.ResponseFile;
import com.uoc.inmo.query.api.response.ResponsePrice;
import com.uoc.inmo.query.entity.inmueble.InmuebleSubscription;
import com.uoc.inmo.query.entity.inmueble.InmuebleSubscriptionPK;
import com.uoc.inmo.query.repository.InmuebleSubscriptionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.NonNull;
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

    @Value("${service.url.createInmuebleSubscription}")
    private String createInmuebleSubscriptionUrl;

    @Value("${service.url.deleteInmuebleSubscription}")
    private String deleteInmuebleSubscriptionUrl;

    @Value("${service.url.checkInmuebleSubscription}")
    private String checkInmuebleSubscriptionUrl;

    @NonNull
    InmuebleSubscriptionRepository inmuebleSubscriptionRepository;

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

    @Override
    public Boolean addInmuebleSubscription(UUID idInmueble, String email){
        
        RequestInmuebleSubscription request = new RequestInmuebleSubscription(idInmueble, email);
        return addInmuebleSubscription(request);
    }

    @Override
    public Boolean addInmuebleSubscription(RequestInmuebleSubscription request){
        ResponseEntity<Boolean> response = restTemplate.postForEntity(createInmuebleSubscriptionUrl, request, Boolean.class);

        if(response.hasBody() && response.getStatusCode().equals(HttpStatus.OK)){
            return true;
        }
        
        return false;
    }

    @Override
    public Boolean deleteInmuebleSubscription(UUID idInmueble, String email){
        Optional<InmuebleSubscription> subscription = inmuebleSubscriptionRepository.findById(new InmuebleSubscriptionPK(idInmueble, email));

        if(!subscription.isPresent())
            return false;

        RequestInmuebleSubscription request = new RequestInmuebleSubscription(subscription.get().getAggregateId(), 
                                                                                idInmueble, 
                                                                                email);

        return deleteInmuebleSubscription(request);
    }

    @Override
    public Boolean deleteInmuebleSubscription(RequestInmuebleSubscription request){
        ResponseEntity<Boolean> response = restTemplate.postForEntity(deleteInmuebleSubscriptionUrl, request, Boolean.class);

        if(response.hasBody() && response.getStatusCode().equals(HttpStatus.OK)){
            return true;
        }
        
        return false;
    }

    @Override
    public Boolean checkInmuebleSubscription(UUID inmuebleId, String email){
        var url = checkInmuebleSubscriptionUrl+"?inmuebleId="+inmuebleId+"&email="+email;

        ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);

        if(response.hasBody())
            return response.getBody();
        return false;
    }
    
}
