package com.uoc.inmo.gui;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("gui")
public class GuiConfiguration {

    // @EventListener(ApplicationReadyEvent.class)
    // public void helloHub(ApplicationReadyEvent event) {
    //     QueryGateway queryGateway = event.getApplicationContext().getBean(QueryGateway.class);
    //     queryGateway.query(new CountInmuebleSummariesQuery(),
    //                        ResponseTypes.instanceOf(CountInmuebleSummariesResponse.class));
    // }
}
