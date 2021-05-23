package com.uoc.inmo.query.entity.user;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "inmobiliaria", schema = "public")
public class Inmobiliaria {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 200)
    private String name;

    @Column(length = 20)
    private String cif;
    
    @Column
    private String phone;

    @Column(length = 200)
    private String web;

    @Column(length = 200)
    private String address;

    @Column(length = 200)
    private String description;

    @OneToOne
    @JoinColumn(name = "email", updatable = false, nullable = false)
    private User user;
}
