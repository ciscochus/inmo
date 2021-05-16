package com.uoc.inmo.gui.service;

import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.query.api.response.ResponseFile;

public interface GuiInmuebleService {
    
    public RequestInmueble createInmueble(RequestInmueble request);
    
    public RequestInmueble updateInmueble(RequestInmueble request);

    public boolean deleteInmueble(UUID idInmueble);

    public ResponseFile getImage(UUID idImage);
}
