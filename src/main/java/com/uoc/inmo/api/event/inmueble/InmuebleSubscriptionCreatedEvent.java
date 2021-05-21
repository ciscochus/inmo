package com.uoc.inmo.api.event.inmueble;

import java.util.UUID;

import com.uoc.inmo.api.event.BaseEvent;
import com.uoc.inmo.command.inmueble.CreateInmuebleSubscriptionCommand;

import lombok.Data;

@Data
public class InmuebleSubscriptionCreatedEvent extends BaseEvent<UUID>{
    
    public UUID inmuebleId;
    public String email;

    public InmuebleSubscriptionCreatedEvent(UUID id) {
        super(id);
    }

    public InmuebleSubscriptionCreatedEvent(UUID id, UUID inmuebleId, String email) {
        super(id);
        this.inmuebleId = inmuebleId;
        this.email = email;
    }



    public InmuebleSubscriptionCreatedEvent(CreateInmuebleSubscriptionCommand command) {
        super(command.id);

        this.inmuebleId = command.inmuebleId;
        this.email = command.email;
    }
}
