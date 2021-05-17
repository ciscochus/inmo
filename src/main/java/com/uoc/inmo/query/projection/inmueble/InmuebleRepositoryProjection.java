package com.uoc.inmo.query.projection.inmueble;

import java.sql.Blob;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.uoc.inmo.api.event.inmueble.InmuebleCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleDeletedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleUpdatedEvent;
import com.uoc.inmo.command.api.request.RequestFile;
import com.uoc.inmo.query.CountChangedUpdate;
import com.uoc.inmo.query.CountInmuebleSummariesQuery;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.CountInmuebleSummariesResponse;
import com.uoc.inmo.query.entity.inmueble.InmuebleImages;
import com.uoc.inmo.query.entity.inmueble.InmueblePriceHistory;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmueblePriceHistoryRepository;
import com.uoc.inmo.query.repository.InmuebleSummaryRepository;
import com.uoc.inmo.query.utils.ConvertionUtils;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    private final InmueblePriceHistoryRepository inmueblePriceHistoryRepository;

    @NonNull
    private final QueryUpdateEmitter queryUpdateEmitter;

    @NonNull
    private final EntityManager entityManager;

    @EventHandler
    public void on(InmuebleCreatedEvent event){
        InmuebleSummary entity = new InmuebleSummary();

        entity.setId(event.getId());
        
        entity.setTitle(event.getTitle());
		entity.setAddress(event.getAddress());
		entity.setPrice(event.getPrice());
		entity.setArea(event.getArea());
		entity.setGarage(event.getGarage());
		entity.setPool(event.getPool());
		entity.setRooms(event.getRooms());
		entity.setBaths(event.getBaths());
        entity.setType(event.getType());
		entity.setDescription(event.getDescription());

        entity.setEmail(event.getEmail());

        entity.setCreated(new Date());
        entity.setUpdated(new Date());

        entity.setImages(convertToInmuebleImagesList(event.getImages(), entity));

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
    public void on(InmuebleUpdatedEvent event){
        Optional<InmuebleSummary> entity = inmuebleSummaryRepository.findById(event.getId());

        if(entity.isPresent()){

            boolean isPriceChanged = isPriceChanged(entity.get(), event);

            InmuebleSummary inmueble = override(entity.get(), event);

            if(isPriceChanged){
                inmueble.setPriceChanged(true);

                InmueblePriceHistory history = new InmueblePriceHistory(event.getId(), inmueble.getPrice(), inmueble.getUpdated());
                inmueblePriceHistoryRepository.save(history); 
            }

            inmuebleSummaryRepository.save(inmueble);

            queryUpdateEmitter.emit(CountInmuebleSummariesQuery.class, 
                query -> event.getId().toString().startsWith(""), 
                new CountChangedUpdate());

            queryUpdateEmitter.emit(FetchInmuebleSummariesQuery.class,
                query -> event.getId().toString().startsWith(""),
                inmueble);
        }
    }

    @EventHandler
    public void on(InmuebleDeletedEvent event){
        Optional<InmuebleSummary> entity = inmuebleSummaryRepository.findById(event.getId());

        if(entity.isPresent()){
            inmuebleSummaryRepository.delete(entity.get());
            
            queryUpdateEmitter.emit(CountInmuebleSummariesQuery.class, 
                query -> event.getId().toString().startsWith(""), 
                new CountChangedUpdate());

            queryUpdateEmitter.emit(FetchInmuebleSummariesQuery.class,
                query -> event.getId().toString().startsWith(""),
                entity.get());
        }
        
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

    private InmuebleSummary override(InmuebleSummary inmueble, InmuebleUpdatedEvent event){

        if(StringUtils.hasText(event.getTitle()))
            inmueble.setTitle(event.getTitle());
         
        if(StringUtils.hasText(event.getAddress()))    
		    inmueble.setAddress(event.getAddress());

		inmueble.setPrice(event.getPrice());
		inmueble.setArea(event.getArea());
		inmueble.setGarage(event.getGarage());
		inmueble.setPool(event.getPool());
		inmueble.setRooms(event.getRooms());
		inmueble.setBaths(event.getBaths());
        inmueble.setType(event.getType());

        if(StringUtils.hasText(event.getDescription()))
		    inmueble.setDescription(event.getDescription());

        inmueble.setImages(convertToInmuebleImagesList(event.getImages(), inmueble));
        
        inmueble.setUpdated(new Date());

        return inmueble;
    }

    private boolean isPriceChanged(InmuebleSummary inmueble, InmuebleUpdatedEvent event){
        
        if(inmueble != null && event != null){
            double oldPrice = inmueble.getPrice();
            double newPrice = event.getPrice();

            return oldPrice != newPrice;
        }

        return false;
    }

    private List<InmuebleImages>  convertToInmuebleImagesList(List<RequestFile> requestList, InmuebleSummary entity){
        List<InmuebleImages> images = new ArrayList<>();
        
        if(CollectionUtils.isEmpty(requestList))
            return images;

        for (RequestFile requestFile : requestList) {

            Blob content = ConvertionUtils.toBlob(requestFile.getContent());

            if(content != null){
                InmuebleImages image = new InmuebleImages();

                image.setId(requestFile.getId());
                image.setName(requestFile.getName());
                image.setMimeType(requestFile.getMimeType());
                image.setContent(content);
                image.setInmueble(entity);
                
                image.setCreated(new Date());

                images.add(image);
            }
        }

        return images;
    }
}
