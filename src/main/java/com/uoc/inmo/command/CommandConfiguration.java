package com.uoc.inmo.command;

import org.axonframework.common.caching.Cache;
import org.axonframework.common.caching.WeakReferenceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("command")
public class CommandConfiguration {

    @Bean
    public Cache cache() {
        return new WeakReferenceCache();
    }
}
