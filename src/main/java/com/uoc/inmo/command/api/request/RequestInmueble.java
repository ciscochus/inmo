package com.uoc.inmo.command.api.request;

import java.util.UUID;

import lombok.Data;

@Data
public class RequestInmueble {
    
    private UUID id;
    
    private String title;
    private String address;

    private double price;
    private double area;
    private String type;

    private Boolean garage;
    private Boolean pool;

    private Integer rooms;
    private Integer baths;

    private String description;
}
