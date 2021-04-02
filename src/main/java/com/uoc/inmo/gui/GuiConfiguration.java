package com.uoc.inmo.gui;

import com.uoc.inmo.query.CountInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.CountInmuebleSummariesResponse;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("gui")
public class GuiConfiguration {

    @EventListener(ApplicationReadyEvent.class)
    public void helloHub(ApplicationReadyEvent event) {
        QueryGateway queryGateway = event.getApplicationContext().getBean(QueryGateway.class);
        queryGateway.query(new CountInmuebleSummariesQuery(),
                           ResponseTypes.instanceOf(CountInmuebleSummariesResponse.class));
    }
}
