package com.uoc.inmo.query.repository;

import java.util.UUID;

import com.uoc.inmo.query.entity.user.Inmobiliaria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmobiliariaRepository extends JpaRepository<Inmobiliaria, UUID>{
    
}
