package com.uoc.inmo.query.repository;

import java.util.UUID;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleSummaryRepository extends JpaRepository<InmuebleSummary, UUID>{
    
}
