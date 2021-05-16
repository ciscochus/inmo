package com.uoc.inmo.api.event.inmueble;

import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;

public class InmuebleUpdatedEvent extends InmuebleCreatedEvent {

    public InmuebleUpdatedEvent(CreateInmuebleCommand command) {
        super(command.id);

        this.title = command.title;
        this.address = command.address;
        this.price = command.price;
        this.area = command.area;
        this.type = command.type;
        this.garage = command.garage;
        this.pool = command.pool;
        this.rooms = command.rooms;
        this.baths = command.baths;
        this.description = command.description;
        this.images = command.images;
    }
    
}
