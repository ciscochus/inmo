package com.uoc.inmo.api.event;

import lombok.Data;

@Data
public class BaseEvent<T> {

    public final T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}