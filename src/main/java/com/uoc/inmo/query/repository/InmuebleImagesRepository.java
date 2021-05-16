package com.uoc.inmo.query.repository;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.query.entity.inmueble.InmuebleImages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleImagesRepository extends JpaRepository<InmuebleImages, UUID>, JpaSpecificationExecutor<InmuebleImages>{

    public List<InmuebleImages> findByInmuebleId(UUID id);
    
}
