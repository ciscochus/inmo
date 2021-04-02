package com.uoc.inmo.query.entity.inmueble;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "INMUEBLE")
@NamedQueries({
    @NamedQuery(name = "InmuebleSummary.fetch",
            query = "SELECT c FROM InmuebleSummary c WHERE c.id LIKE CONCAT(:idStartsWith, '%') ORDER BY c.id"),
    @NamedQuery(name = "InmuebleSummary.count",
            query = "SELECT COUNT(c) FROM InmuebleSummary c WHERE c.id LIKE CONCAT(:idStartsWith, '%')")})
public class InmuebleSummary {
    
    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Column
    private double price;

    @Column
    private Date created;

    @Column
    private Date updated;

}
