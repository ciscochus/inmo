package com.uoc.inmo.query.repository;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.query.entity.inmueble.InmueblePriceHistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InmueblePriceHistoryRepository extends JpaRepository<InmueblePriceHistory, UUID>{
    
    List<InmueblePriceHistory> findInmueblePriceHistoriesByIdInmuebleOrderByCreatedDesc(UUID idInmueble);
}
