package com.uoc.inmo.api.event.inmueble;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.api.event.BaseEvent;
import com.uoc.inmo.command.api.request.RequestFile;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;

import lombok.Data;

@Data
public class InmuebleCreatedEvent extends BaseEvent<UUID>{

    public String title;
    public String address;

    public double price;
    public double area;
    public String type;

    public Boolean garage;
    public Boolean pool;

    public Integer rooms;
    public Integer baths;

    public String description;
    public String email;

    public List<RequestFile> images;

    public InmuebleCreatedEvent(UUID id){
        super(id);
    }

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
        this.images = command.images;
    }
    
}
