package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import lombok.Data;

@Data
public class DeleteInmuebleSubscriptionCommand extends CreateInmuebleSubscriptionCommand{

    public DeleteInmuebleSubscriptionCommand(UUID id, UUID inmuebleId, String email) {
        super(id, inmuebleId, email);
    }
}
