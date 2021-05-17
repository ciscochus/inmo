package com.uoc.inmo.query.entity.inmueble;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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

    @Column(length = 200, columnDefinition = "default ''")
    private String title;

    @Column(length = 200, columnDefinition = "default ''")
    private String address;

    @Column(columnDefinition = "default 0")
    private double price;

    @Column(columnDefinition = "default 0")
    private double area;

    @Column(length = 50, columnDefinition = "default ''")
    private String type;

    @Column(columnDefinition = "default false")
    private Boolean garage = false;

    @Column(columnDefinition = "default false")
    private Boolean pool = false;

    @Column(columnDefinition = "default 0")
    private int rooms;

    @Column(columnDefinition = "default 0")
    private int baths;

    @Column(length = 2000, columnDefinition = "default ''")
    private String description;

    @Column(name = "email", length = 200)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inmueble", fetch = FetchType.EAGER)
    List<InmuebleImages> images = new ArrayList<>();


    @Column
    private Date created;

    @Column
    private Date updated;

    @Column(columnDefinition = "default false")
    private Boolean priceChanged = false;

    
}
