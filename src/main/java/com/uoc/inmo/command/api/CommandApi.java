package com.uoc.inmo.command.api;


import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleCommand;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController(value = "/api/command")
@RequiredArgsConstructor
public class CommandApi {
    
    @Autowired
    @NonNull
    private final CommandGateway commandGateway;


    @PostMapping("/inmueble")
	public String addInmueble(@RequestBody RequestInmueble request) {
		commandGateway.sendAndWait(new CreateInmuebleCommand(request.getPrice()));
		return "Saved";
	}

    @DeleteMapping("/inmueble")
    public String deleteInmueble(@RequestBody RequestInmueble request) {
		commandGateway.sendAndWait(new DeleteInmuebleCommand(request.getId()));
		return "Removed";
	}
}
