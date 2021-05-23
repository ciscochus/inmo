package com.uoc.inmo.query.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user", schema = "public")
public class User {

    public static final String TYPE_PARTICULAR = "PARTICULAR";
    public static final String TYPE_PROFESIONAL = "PROFESIONAL";

    @Id
    @Column(name = "email", length = 200)
    private String email;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 50)
    private String tipo;

    @OneToOne(mappedBy = "user")
    private Inmobiliaria inmobiliaria;

    @OneToOne(mappedBy = "user")
    private Particular particular;
}
