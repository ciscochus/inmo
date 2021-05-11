package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import lombok.Data;

@Data
public class UpdateInmuebleCommand extends CreateInmuebleCommand {

    public UpdateInmuebleCommand(UUID id) {
        super(id);
    }
}
