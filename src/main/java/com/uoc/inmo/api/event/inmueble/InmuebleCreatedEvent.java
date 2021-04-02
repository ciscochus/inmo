package com.uoc.inmo.api.event.inmueble;

import java.util.UUID;

import com.uoc.inmo.api.event.BaseEvent;

import lombok.Data;

@Data
public class InmuebleCreatedEvent extends BaseEvent<UUID>{

    public final double price;

    public InmuebleCreatedEvent(UUID id, double price) {
        super(id);
        this.price = price;
    }
    
}
