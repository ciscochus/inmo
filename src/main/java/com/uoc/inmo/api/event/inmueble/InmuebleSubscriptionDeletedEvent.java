package com.uoc.inmo.api.event.inmueble;

import com.uoc.inmo.command.inmueble.DeleteInmuebleSubscriptionCommand;

import lombok.Data;

@Data
public class InmuebleSubscriptionDeletedEvent extends InmuebleSubscriptionCreatedEvent{

    public InmuebleSubscriptionDeletedEvent(DeleteInmuebleSubscriptionCommand command) {
        super(command.id, command.inmuebleId, command.email);
    }
    
}
