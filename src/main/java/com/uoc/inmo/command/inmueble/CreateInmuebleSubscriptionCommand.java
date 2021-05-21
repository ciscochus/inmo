package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import com.uoc.inmo.command.BaseCommand;

import lombok.Data;

@Data
public class CreateInmuebleSubscriptionCommand extends BaseCommand<UUID>{

    public UUID inmuebleId;
    public String email;

    public CreateInmuebleSubscriptionCommand(UUID id) {
        super(id);
    }

    public CreateInmuebleSubscriptionCommand(UUID inmuebleId, String email) {
        super(UUID.randomUUID());
        this.inmuebleId = inmuebleId;
        this.email = email;
    }

    public CreateInmuebleSubscriptionCommand(UUID id, UUID inmuebleId, String email) {
        super(id);
        this.inmuebleId = inmuebleId;
        this.email = email;
    }
    
}
