package com.uoc.inmo.command.api.request;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestInmuebleSubscription {
    
    private UUID subscriptionId;
    private UUID idInmueble;
    private String email;
    
    public RequestInmuebleSubscription(UUID idInmueble, String email) {
        this.idInmueble = idInmueble;
        this.email = email;
    }

    public RequestInmuebleSubscription(UUID subscriptionId, UUID idInmueble, String email) {
        this.subscriptionId = subscriptionId;
        this.idInmueble = idInmueble;
        this.email = email;
    }

}
