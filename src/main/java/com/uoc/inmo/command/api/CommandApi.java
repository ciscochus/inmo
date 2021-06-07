package com.uoc.inmo.command.api;


import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.command.api.request.RequestInmuebleSubscription;
import com.uoc.inmo.command.inmueble.CreateInmuebleCommand;
import com.uoc.inmo.command.inmueble.CreateInmuebleSubscriptionCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleCommand;
import com.uoc.inmo.command.inmueble.DeleteInmuebleSubscriptionCommand;
import com.uoc.inmo.command.inmueble.UpdateInmuebleCommand;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/command")
@RequiredArgsConstructor
public class CommandApi {
    
    @Autowired
    @NonNull
    private final CommandGateway commandGateway;


    @PostMapping("/inmueble")
	public String addInmueble(@RequestBody RequestInmueble request) {

		if(!StringUtils.hasText(request.getEmail()))
			return null;

		CreateInmuebleCommand command = new CreateInmuebleCommand();

		command.setTitle(request.getTitle());
		command.setAddress(request.getAddress());
		command.setPrice(request.getPrice());
		command.setArea(request.getArea());
		command.setGarage(request.getGarage());
		command.setPool(request.getPool());
		command.setRooms(request.getRooms());
		command.setBaths(request.getBaths());
		command.setDescription(request.getDescription());
		command.setEmail(request.getEmail());

		if(!CollectionUtils.isEmpty(request.getImages()))
			command.setImages(request.getImages());

		commandGateway.sendAndWait(command);
		return "Saved";
	}

	@PutMapping("/inmueble")
	public String updateInmueble(@RequestBody RequestInmueble request) {

		if(!StringUtils.hasText(request.getEmail()))
			return null;

		if(request.getId() == null)
			return null;

		UpdateInmuebleCommand command = new UpdateInmuebleCommand(request.getId());

		command.setTitle(request.getTitle());
		command.setAddress(request.getAddress());
		command.setPrice(request.getPrice());
		command.setArea(request.getArea());
		command.setGarage(request.getGarage());
		command.setPool(request.getPool());
		command.setRooms(request.getRooms());
		command.setBaths(request.getBaths());
		command.setDescription(request.getDescription());
		command.setEmail(request.getEmail());

		if(!CollectionUtils.isEmpty(request.getImages()))
			command.setImages(request.getImages());

		commandGateway.sendAndWait(command);
		return "Saved";
	}



    @DeleteMapping("/inmueble/{id}")
    public String deleteInmueble(@PathVariable(value = "id") UUID inmuebleId) {
		commandGateway.sendAndWait(new DeleteInmuebleCommand(inmuebleId));
		return "Removed";
	}

	@PostMapping("/inmuebleSubscription")
	public Boolean addInmuebleSubcription(@RequestBody RequestInmuebleSubscription request) {
		commandGateway.sendAndWait(new CreateInmuebleSubscriptionCommand(request.getIdInmueble(), request.getEmail()));
		return true;
	}

	@PostMapping("/deleteInmuebleSubscription")
    public Boolean deleteInmuebleSubscription(@RequestBody RequestInmuebleSubscription request) {
		commandGateway.sendAndWait(new DeleteInmuebleSubscriptionCommand(request.getSubscriptionId(), 
																		request.getIdInmueble(), 
																		request.getEmail()));
		return true;
	}
}
