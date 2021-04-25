package com.uoc.inmo.query.repository;

import com.uoc.inmo.query.entity.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    
    User findByEmailIgnoreCase(String email);

}
