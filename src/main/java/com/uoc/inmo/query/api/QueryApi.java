package com.uoc.inmo.query.api;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.api.response.ResponseFile;
import com.uoc.inmo.query.api.response.ResponsePrice;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.service.InmuebleService;
import com.uoc.inmo.query.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/query")
@RequiredArgsConstructor
public class QueryApi {
    
    @Autowired
    @NonNull
    private UserService userService;

    @Autowired
    @NonNull
    private InmuebleService inmuebleService;

    @PostMapping("/user")
	public String addUser(@RequestBody RequestUser request) {
		User user = userService.createUser(request);

        if(user == null)
            return "Error";
            
		return "Saved";
	}

    @GetMapping("/image/{id}")
    public ResponseFile getImage(@PathVariable UUID id){
        return inmuebleService.getImage(id);
    }

    @GetMapping(value = "/priceHistory/{inmuebleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ResponsePrice> getInmueblePriceHistory(@PathVariable UUID inmuebleId){
        return inmuebleService.getInmueblePriceHistory(inmuebleId);
    }

    @GetMapping("/inmuebleImages/{inmuebleId}")
    public List<ResponseFile> getInmuebleImages(@PathVariable UUID inmuebleId){
        return inmuebleService.getInmuebleImages(inmuebleId);
    }

    @GetMapping("/checkInmuebleSubscription")
    public Boolean checkInmuebleSubscription(@RequestParam UUID inmuebleId, @RequestParam String email){
        return inmuebleService.checkInmuebleSubscription(inmuebleId, email);
    }
}
