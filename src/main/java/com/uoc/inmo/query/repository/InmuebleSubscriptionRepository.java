package com.uoc.inmo.query.repository;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.query.entity.inmueble.InmuebleSubscription;
import com.uoc.inmo.query.entity.inmueble.InmuebleSubscriptionPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InmuebleSubscriptionRepository extends JpaRepository<InmuebleSubscription, InmuebleSubscriptionPK>{
    
    List<InmuebleSubscription> findAllByIdIdInmueble(UUID idInmueble);
}
