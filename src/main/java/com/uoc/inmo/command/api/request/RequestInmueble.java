package com.uoc.inmo.command.api.request;

import java.util.UUID;

import lombok.Data;

@Data
public class RequestInmueble {
    
    private UUID id;
    private double price;
}
