package com.uoc.inmo.query.service;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.entity.user.Inmobiliaria;
import com.uoc.inmo.query.entity.user.Particular;
import com.uoc.inmo.query.entity.user.User;

public interface UserService {
    
    public User createUser(RequestUser request);
    public User createUser(User user);

    public User createInmobiliaria(User user, Inmobiliaria inmobiliaria);
    public User createParticular(User user, Particular particular);

    public User getUser(String email);
}
