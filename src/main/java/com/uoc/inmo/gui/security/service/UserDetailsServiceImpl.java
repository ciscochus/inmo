package com.uoc.inmo.gui.security.service;

import java.util.Collections;

import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.repository.UserRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    @NonNull
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(email);
		if (null == user) {
			throw new UsernameNotFoundException("No user present with email: " + email);
		} else {
            String tipo = user.getTipo();
            if(!StringUtils.hasText(tipo))
                tipo = User.ROLE_PARTICULAR;

			return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority(tipo)));
		
            }

            
    }
    
}
