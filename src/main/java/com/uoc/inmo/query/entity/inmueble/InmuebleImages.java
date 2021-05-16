package com.uoc.inmo.query.entity.inmueble;

import java.sql.Blob;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import lombok.Data;

@Entity
@Data
@Table(name = "InmuebleImages")
public class InmuebleImages {
    
    @Id
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mimeType", nullable = false)
    private String mimeType;

    @Lob
    @Column(name = "content", nullable = false)
    private Blob content;

    @Column(name = "created")
    private Date created;

    @ManyToOne
    @JoinColumn(name="inmuebleId", referencedColumnName = "id")
    private InmuebleSummary inmueble;
}
