package com.uoc.inmo.gui.service;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.command.api.request.RequestInmuebleSubscription;
import com.uoc.inmo.query.api.response.ResponseFile;
import com.uoc.inmo.query.api.response.ResponsePrice;

public interface GuiInmuebleService {
    
    public RequestInmueble createInmueble(RequestInmueble request);
    
    public RequestInmueble updateInmueble(RequestInmueble request);

    public boolean deleteInmueble(UUID idInmueble);

    public ResponseFile getImage(UUID idImage);

    public List<ResponsePrice> getInmueblePriceHistory(UUID inmuebleId);

    public Boolean addInmuebleSubscription(RequestInmuebleSubscription request);

    public Boolean addInmuebleSubscription(UUID idInmueble, String email);

    public Boolean deleteInmuebleSubscription(RequestInmuebleSubscription request);

    public Boolean deleteInmuebleSubscription(UUID idInmueble, String email);

    public Boolean checkInmuebleSubscription(UUID inmuebleId, String email);
}
