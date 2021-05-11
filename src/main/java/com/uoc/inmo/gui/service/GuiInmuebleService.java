package com.uoc.inmo.gui.service;

import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;

public interface GuiInmuebleService {
    
    public RequestInmueble createInmueble(RequestInmueble request);

    public boolean deleteInmueble(UUID idInmueble);
}
