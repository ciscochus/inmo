package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import com.uoc.inmo.command.BaseCommand;

import lombok.Data;

@Data
public class CreateInmuebleCommand extends BaseCommand<UUID> {

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

    public CreateInmuebleCommand(){
        super(UUID.randomUUID());
    }

    public CreateInmuebleCommand(String title, String address, double price, double area, String type,
            Boolean garage, Boolean pool, Integer rooms, Integer baths, String description, String email) {
        
        super(UUID.randomUUID());
        this.title = title;
        this.address = address;
        this.price = price;
        this.area = area;
        this.type = type;
        this.garage = garage;
        this.pool = pool;
        this.rooms = rooms;
        this.baths = baths;
        this.description = description;
        this.email = email;
    }

    
    
}
