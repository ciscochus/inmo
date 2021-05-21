package com.uoc.inmo.query.entity.inmueble;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "INMUEBLE_SUBSCRIPTION")
@NoArgsConstructor
public class InmuebleSubscription {
    
    @EmbeddedId
    private InmuebleSubscriptionPK id;

    @Type(type = "uuid-char")
    @Column(name = "aggregateId", nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public InmuebleSubscription(UUID idInmueble, String email, UUID aggregateId) {
        this.id = new InmuebleSubscriptionPK(idInmueble, email);
        this.aggregateId = aggregateId;
        this.created = new Date();
    }

    
}
