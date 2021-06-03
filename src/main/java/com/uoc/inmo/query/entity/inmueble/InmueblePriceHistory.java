package com.uoc.inmo.query.entity.inmueble;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "INMUEBLE_PRICE_HISTORY")
@NoArgsConstructor
public class InmueblePriceHistory {
    
    @Id
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Type(type = "uuid-char")
    @Column(name = "idInmueble", nullable = false)
    private UUID idInmueble;

    public InmueblePriceHistory(UUID idInmueble, double price, Date created) {
        this.id = UUID.randomUUID();
        this.price = price;
        this.created = created;
        this.idInmueble = idInmueble;
    }

    
}
