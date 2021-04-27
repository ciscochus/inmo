package com.uoc.inmo.query.api;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController(value = "/api/query")
@RequiredArgsConstructor
public class QueryApi {
    
    @Autowired
    @NonNull
    private UserService userService;

    @PostMapping("/user")
	public String addInmueble(@RequestBody RequestUser request) {
		User user = userService.createUser(request);

        if(user == null)
            return "Error";
            
		return "Saved";
	}
}
