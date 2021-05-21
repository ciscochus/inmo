package com.uoc.inmo.command.aggregates;

import java.util.UUID;

import com.uoc.inmo.api.event.inmueble.InmuebleSubscriptionCreatedEvent;
import com.uoc.inmo.api.event.inmueble.InmuebleSubscriptionDeletedEvent;
import com.uoc.inmo.command.inmueble.CreateInmuebleSubscriptionCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleSubscriptionCommand;

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
public class InmuebleSubscriptionAggregate {
    
    @AggregateIdentifier
    private UUID id;

    private UUID inmuebleId;
    private String email;

    @CommandHandler
    public InmuebleSubscriptionAggregate(CreateInmuebleSubscriptionCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleSubscriptionCreatedEvent(command));
    }

    @CommandHandler
    public void handle(DeleteInmuebleSubscriptionCommand command){
        log.entry(command);
        AggregateLifecycle.apply(new InmuebleSubscriptionDeletedEvent(command));
    }

    @EventSourcingHandler
    public void on(InmuebleSubscriptionCreatedEvent event){
        log.entry(event);
        id = event.id;
        inmuebleId = event.inmuebleId;
        email = event.email;
        
        log.trace("new state of aggregate: {}", this);
    }

    @EventSourcingHandler
    public void on(InmuebleSubscriptionDeletedEvent event){
        log.entry(event);
        id = event.id;
        AggregateLifecycle.markDeleted();
        log.trace("new state of aggregate: {}", this);
    }
}
