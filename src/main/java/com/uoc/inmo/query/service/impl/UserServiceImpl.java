package com.uoc.inmo.query.service.impl;

import javax.persistence.EntityExistsException;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.repository.UserRepository;
import com.uoc.inmo.query.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    @NonNull
    private final UserRepository userRepository;

    @NonNull
	private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RequestUser request){
        User user = converToUser(request);

        return createUser(user);
    }

    @Override
    public User createUser(User user){

        if(user != null){

            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            if(userRepository.existsById(user.getEmail()))
                throw new EntityExistsException();

            return userRepository.saveAndFlush(user);
        }

        return null;
    }



    private User converToUser(RequestUser request){

        if(request == null)
            return null;

        if(!StringUtils.hasText(request.getEmail()) ||
        !StringUtils.hasText(request.getPassword()))
            return null;

        User user = new User();
        user.setEmail(request.getEmail());

        user.setPassword(request.getPassword());
        user.setTipo(request.getTipo());

        return user;
        
    }
}
