package com.uoc.inmo.query.entity.inmueble;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class InmuebleSubscriptionPK implements Serializable{
    
    @Type(type = "uuid-char")
    @Column(name = "idInmueble", nullable = false)
    private UUID idInmueble;

    @Column(name = "email", nullable = false)
    private String email;

    public InmuebleSubscriptionPK(UUID idInmueble, String email) {
        this.idInmueble = idInmueble;
        this.email = email;
    }
}
