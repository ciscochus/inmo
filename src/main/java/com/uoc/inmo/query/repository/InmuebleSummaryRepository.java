package com.uoc.inmo.query.repository;

import java.util.UUID;

import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleSummaryRepository extends CrudRepository<InmuebleSummary, UUID>{
    
}
