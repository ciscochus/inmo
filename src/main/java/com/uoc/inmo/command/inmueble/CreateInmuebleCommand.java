package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import com.uoc.inmo.command.BaseCommand;

public class CreateInmuebleCommand extends BaseCommand<UUID> {

    public final double price;

    public CreateInmuebleCommand(double price) {
        super(UUID.randomUUID());
        this.price = price;
    }
    
}
