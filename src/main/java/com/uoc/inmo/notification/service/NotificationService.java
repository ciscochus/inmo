package com.uoc.inmo.notification.service;

import com.uoc.inmo.notification.api.request.RequestMessage;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;

public interface NotificationService {
    
    public void enviarCorreo(String[] bcc, String subject, String message);

    public void notificarCambioPrecio(InmuebleSummary inmueble, double oldPrice, double newPrice);

    public Boolean sendMessage(RequestMessage request);
}
