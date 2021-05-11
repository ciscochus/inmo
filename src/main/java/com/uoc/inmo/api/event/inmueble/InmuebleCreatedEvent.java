package com.uoc.inmo.api.event.inmueble;

import java.util.UUID;

import com.uoc.inmo.api.event.BaseEvent;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;

import lombok.Data;

@Data
public class InmuebleCreatedEvent extends BaseEvent<UUID>{

    public final String title;
    public final String address;

    public final double price;
    public final double area;
    public final String type;

    public final Boolean garage;
    public final Boolean pool;

    public final Integer rooms;
    public final Integer baths;

    public final String description;
    public final String email;

    public InmuebleCreatedEvent(CreateInmuebleCommand command){

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
        this.email = command.email;
    }
    
}
