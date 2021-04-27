package com.uoc.inmo.query.service;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.entity.user.User;

public interface UserService {
    
    public User createUser(RequestUser request);
}
