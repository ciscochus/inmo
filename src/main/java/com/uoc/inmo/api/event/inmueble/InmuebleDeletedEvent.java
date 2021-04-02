package com.uoc.inmo.api.event.inmueble;

import java.util.UUID;

import com.uoc.inmo.api.event.BaseEvent;

public class InmuebleDeletedEvent extends BaseEvent<UUID> {

    public InmuebleDeletedEvent(UUID id) {
        super(id);
    }
}
