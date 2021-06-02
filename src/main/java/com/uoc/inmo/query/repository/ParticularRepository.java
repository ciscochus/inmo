package com.uoc.inmo.query.repository;

import java.util.UUID;

import com.uoc.inmo.query.entity.user.Particular;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticularRepository extends JpaRepository<Particular, UUID>{
    
}
