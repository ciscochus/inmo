package com.uoc.inmo.notification.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.uoc.inmo.notification.service.NotificationService;
import com.uoc.inmo.query.entity.inmueble.InmuebleSubscription;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.repository.InmuebleSubscriptionRepository;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceServiceImpl implements NotificationService{

    @NonNull
    private final InmuebleSubscriptionRepository inmuebleSubscriptionRepository;

    @NonNull
    private JavaMailSender mailSender;

    @Override
    public void notificarCambioPrecio(InmuebleSummary inmueble, double oldPrice, double newPrice) {
        List<InmuebleSubscription> subscriptions = inmuebleSubscriptionRepository.findAllByIdIdInmueble(inmueble.getId());

        if(!CollectionUtils.isEmpty(subscriptions)){
            List<String> emails = new ArrayList<>();

            for (InmuebleSubscription inmuebleSubscription : subscriptions) {
                emails.add(inmuebleSubscription.getId().getEmail());
            }

            String subject = "Precio actualizado";
            String message = getCuerpoCambioPrecio(inmueble, oldPrice, newPrice);

            String[] bcc = new String[emails.size()]; 
            enviarCorreo(emails.toArray(bcc), subject, message);
        }
    }

    @Override
    public void enviarCorreo(String[] bcc, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setBcc(bcc);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
        
    }

    private String getCuerpoCambioPrecio(InmuebleSummary inmueble, double oldPrice, double newPrice){

        StringBuilder sb = new StringBuilder("Un inmueble de su lista de subscripciones ha cambiado de precio.");
        sb.append("Id: "+inmueble.getId());
        sb.append("Dirección: "+inmueble.getAddress());
        sb.append(oldPrice+"€ -> "+newPrice+"€");

        return sb.toString();
    }
    
}
