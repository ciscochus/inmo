package com.uoc.inmo.query.service.impl;

import java.util.UUID;

import javax.persistence.EntityExistsException;

import com.uoc.inmo.query.api.request.RequestUser;
import com.uoc.inmo.query.entity.user.Inmobiliaria;
import com.uoc.inmo.query.entity.user.Particular;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.repository.InmobiliariaRepository;
import com.uoc.inmo.query.repository.ParticularRepository;
import com.uoc.inmo.query.repository.UserRepository;
import com.uoc.inmo.query.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final InmobiliariaRepository inmobiliariaRepository;

    @NonNull
    private final ParticularRepository particularRepository;

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

    @Override
    public User createInmobiliaria(User newUser, Inmobiliaria inmobiliaria) {

        User user = createUser(newUser);

        if(user == null)
            return null;

        inmobiliaria.setId(UUID.randomUUID());
        inmobiliaria.setUser(user);

        inmobiliaria = inmobiliariaRepository.saveAndFlush(inmobiliaria);

        user.setInmobiliaria(inmobiliaria);

        return user;
    }

    @Override
    public User createParticular(User newUser, Particular particular) {

        User user = createUser(newUser);

        if(user == null)
            return null;

        particular.setId(UUID.randomUUID());
        particular.setUser(user);

        particular = particularRepository.saveAndFlush(particular);

        user.setParticular(particular);

        return user;
    }
}
