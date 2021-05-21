package com.uoc.inmo.command.aggregates;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.api.event.inmueble.InmuebleCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleDeletedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleSubscriptionCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleSubscriptionDeletedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleUpdatedEvent;
import com.uoc.inmo.command.api.request.RequestFile;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;
import com.uoc.inmo.command.inmueble.CreateInmuebleSubscriptionCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleSubscriptionCommand;
import com.uoc.inmo.command.inmueble.UpdateInmuebleCommand;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.XSlf4j;

@XSlf4j
@Aggregate
@NoArgsConstructor
public class InmuebleAggregate {
    
    @AggregateIdentifier
    private UUID id;

    private String title;
    private String address;

    private double price;
    private double area;
    private String type;

    private Boolean garage;
    private Boolean pool;

    private Integer rooms;
    private Integer baths;

    private String description;

    private String email;

    private List<RequestFile> images;

    @CommandHandler
    public InmuebleAggregate(CreateInmuebleCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleCreatedEvent(command));
    }

    @CommandHandler
    public void handle(UpdateInmuebleCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleUpdatedEvent(command));
    }

    @CommandHandler
    public void handle(DeleteInmuebleCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleDeletedEvent(command.id));
    }

    // @CommandHandler
    // public void handle(CreateInmuebleSubscriptionCommand command){
    //     log.entry(command);
    //     AggregateLifecycle.apply(new InmuebleSubscriptionCreatedEvent(command));
    // }

    // @CommandHandler
    // public void handle(DeleteInmuebleSubscriptionCommand command){
    //     log.entry(command);
    //     AggregateLifecycle.apply(new InmuebleSubscriptionDeletedEvent(command));
    // }

    @EventSourcingHandler
    public void on(InmuebleCreatedEvent event){
        log.entry(event);
        id = event.id;
        title = event.title;
        address = event.address;
        
        price = event.price;
        area = event.area;
        type = event.type;

        garage = event.garage;
        pool = event.pool;

        rooms = event.rooms;
        baths = event.baths;

        description = event.description;

        email = event.email;

        images = event.images;
        
        log.trace("new state of aggregate: {}", this);
    }

    @EventSourcingHandler
    public void on(InmuebleUpdatedEvent event){
        log.entry(event);
        id = event.id;
        title = event.title;
        address = event.address;
        
        price = event.price;
        area = event.area;
        type = event.type;

        garage = event.garage;
        pool = event.pool;

        rooms = event.rooms;
        baths = event.baths;

        description = event.description;

        email = event.email;

        images = event.images;
        
        log.trace("new state of aggregate: {}", this);
    }

    @EventSourcingHandler
    public void on(InmuebleDeletedEvent event){
        log.entry(event);
        id = event.id;
        AggregateLifecycle.markDeleted();
        log.trace("new state of aggregate: {}", this);
    }

    // @EventSourcingHandler
    // public void on(InmuebleSubscriptionCreatedEvent event){
    //     log.entry(event);
    //     // id = event.id;
    //     // inmuebleId = event.inmuebleId;
    //     // email = event.email;
        
    //     // log.trace("new state of aggregate: {}", this);
    // }

    // @EventSourcingHandler
    // public void on(InmuebleSubscriptionDeletedEvent event){
    //     log.entry(event);
    //     // id = event.id;
    //     // log.trace("new state of aggregate: {}", this);
    // }

}
