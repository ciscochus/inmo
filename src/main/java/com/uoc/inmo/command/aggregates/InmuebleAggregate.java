package com.uoc.inmo.command.aggregates;

import java.util.UUID;

import com.uoc.inmo.api.event.inmueble.InmuebleCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleDeletedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleUpdatedEvent;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleCommand;
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
        
        log.trace("new state of aggregate: {}", this);
    }

    @EventSourcingHandler
    public void on(InmuebleDeletedEvent event){
        log.entry(event);
        id = event.id;
        AggregateLifecycle.markDeleted();
        log.trace("new state of aggregate: {}", this);
    }

}
