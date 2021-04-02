package com.uoc.inmo.command.inmueble;

import java.util.UUID;

import com.uoc.inmo.command.BaseCommand;

import lombok.Data;

@Data
public class DeleteInmuebleCommand extends BaseCommand<UUID> {

    public DeleteInmuebleCommand(UUID id) {
        super(id);
    }

    
}
