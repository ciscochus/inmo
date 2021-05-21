package com.uoc.inmo.command;

import com.uoc.inmo.command.aggregates.InmuebleAggregate;
import com.uoc.inmo.command.aggregates.InmuebleSubscriptionAggregate;

import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("command")
public class CommandConfiguration {

    @Bean
    public Repository<InmuebleAggregate> inmuebleAggregateRepository(EventStore eventStore, Cache cache) {
        return EventSourcingRepository.builder(InmuebleAggregate.class)
                                      .cache(cache)
                                      .eventStore(eventStore)
                                      .build();
    }

    @Bean
    public Repository<InmuebleSubscriptionAggregate> inmuebleSubscriptionAggregateRepository(EventStore eventStore, Cache cache) {
        return EventSourcingRepository.builder(InmuebleSubscriptionAggregate.class)
                                      .cache(cache)
                                      .eventStore(eventStore)
                                      .build();
    }

    @Bean
    public Cache cache() {
        return new WeakReferenceCache(); 
    }
}
