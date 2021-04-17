package com.uoc.inmo.query.projection.inmueble;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.uoc.inmo.api.event.inmueble.InmuebleCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleDeletedEvent;
import com.uoc.inmo.query.CountChangedUpdate;
import com.uoc.inmo.query.CountInmuebleSummariesQuery;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.CountInmuebleSummariesResponse;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmuebleSummaryRepository;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;

@RequiredArgsConstructor
@XSlf4j
@Component
public class InmuebleRepositoryProjection {
    
    @NonNull
    private final InmuebleSummaryRepository inmuebleSummaryRepository;

    @NonNull
    private final QueryUpdateEmitter queryUpdateEmitter;

    @NonNull
    private final EntityManager entityManager;

    @EventHandler
    public void on(InmuebleCreatedEvent event){
        InmuebleSummary entity = new InmuebleSummary();

        entity.setId(event.getId());
        entity.setPrice(event.getPrice());
        entity.setCreated(new Date());
        entity.setUpdated(new Date());

        inmuebleSummaryRepository.save(entity);

        queryUpdateEmitter.emit(CountInmuebleSummariesQuery.class, 
            query -> event.getId().toString().startsWith(""), 
            new CountChangedUpdate());

        queryUpdateEmitter.emit(FetchInmuebleSummariesQuery.class,
            query -> event.getId().toString().startsWith(""),
            entity);

        log.trace("new inmueble projection saved: {}", entity);
    }

    @EventHandler
    public void on(InmuebleDeletedEvent event){
        Optional<InmuebleSummary> entity = inmuebleSummaryRepository.findById(event.getId());

        if(entity.isPresent())
        inmuebleSummaryRepository.delete(entity.get());
    }

    @QueryHandler
    public List<InmuebleSummary> handle(FetchInmuebleSummariesQuery query) {
        log.trace("handling {}", query);
        TypedQuery<InmuebleSummary> jpaQuery = entityManager.createNamedQuery("InmuebleSummary.fetch", InmuebleSummary.class);
        jpaQuery.setParameter("idStartsWith", query.getFilter().getIdStartsWith());
        jpaQuery.setFirstResult(query.getOffset());
        jpaQuery.setMaxResults(query.getLimit());

        List<InmuebleSummary> result = new ArrayList<>();
        inmuebleSummaryRepository.findAll().forEach(result::add);

        return log.exit(result);
    }

    @QueryHandler
    public CountInmuebleSummariesResponse handle(CountInmuebleSummariesQuery query) {
        log.trace("handling {}", query);
        // TypedQuery<Long> jpaQuery = entityManager.createNamedQuery("InmuebleSummary.count", Long.class);
        // jpaQuery.setParameter("idStartsWith", query.getFilter().getIdStartsWith());
        
        return new CountInmuebleSummariesResponse(Long.valueOf(inmuebleSummaryRepository.count()).intValue(), Instant.now().toEpochMilli());
    } 

}
