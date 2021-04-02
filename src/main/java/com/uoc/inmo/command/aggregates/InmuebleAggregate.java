package com.uoc.inmo.command.aggregates;

import java.util.UUID;

import com.uoc.inmo.api.event.inmueble.InmuebleCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleDeletedEvent;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleCommand;

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

    private double price;

    @CommandHandler
    public InmuebleAggregate(CreateInmuebleCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleCreatedEvent(command.id, command.price));
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
        price = event.price;
        
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
