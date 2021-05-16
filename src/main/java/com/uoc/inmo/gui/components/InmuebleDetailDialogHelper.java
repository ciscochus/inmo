package com.uoc.inmo.gui.components;

import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class InmuebleDetailDialogHelper {
    
    @Autowired GuiInmuebleService guiInmuebleService;

    public Dialog create(InmuebleSummary inmueble){
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setId("inmueble-detail-dialog");

        if(inmueble == null)
            return dialog;

        //Imagenes
        Div imagenesDiv = new Div();
        imagenesDiv.setId("imagenes-div");

        // barra separadora
        
        // Titulo

        HorizontalLayout tituloLayout = new HorizontalLayout();
        
        Span titulo = new Span(inmueble.getTitle());
        titulo.setClassName("inmueble-title");

        Span address = new Span(inmueble.getAddress());
        address.setClassName("inmueble-address");

        tituloLayout.add(titulo, address);

        Div tituloDiv = new Div(tituloLayout);
        tituloDiv.setId("titulo-div");

        // Superficie - habitaciones - precio

        HorizontalLayout areaLayout = new HorizontalLayout();
        
        Label areaLabel = new Label("Supeficie: ");
        areaLabel.addClassNames("label", "area-label");

        Span areaValue = new Span(Double.toString(inmueble.getArea()));
        areaValue.addClassNames("area-value");

        Label roomsLabel = new Label("Habitaciones: ");
        roomsLabel.addClassNames("label", "rooms-label");

        Span roomsValue = new Span(Integer.toString(inmueble.getRooms()));
        roomsValue.addClassNames("rooms-value");

        Label bathsLabel = new Label("Baños: ");
        bathsLabel.addClassNames("label", "baths-label");

        Span bathsValue = new Span(Integer.toString(inmueble.getBaths()));
        bathsValue.addClassNames("baths-value");

        Span price = new Span(Double.toString(inmueble.getPrice())+" €");
        price.setClassName("inmueble-price");

        areaLayout.add(areaLabel, areaValue, roomsLabel, roomsValue, bathsLabel, bathsValue, price);

        Div areaDiv = new Div(areaLayout);
        areaDiv.setId("area-div");

        // Descripcion
        Div descriptionDiv = new Div(new Text(inmueble.getDescription() != null ? inmueble.getDescription() : ""));
        descriptionDiv.setId("inmueble-description");

        // barra separadora

        // Otras características

        HorizontalLayout otherPropertiesLayout = new HorizontalLayout();
        
        Checkbox garageCheck = new Checkbox("Garaje", inmueble.getGarage() != null && inmueble.getGarage());
        garageCheck.setReadOnly(true);

        Checkbox poolCheck = new Checkbox("Piscina", inmueble.getPool() != null && inmueble.getPool());
        poolCheck.setReadOnly(true);

        otherPropertiesLayout.add(garageCheck, poolCheck);

        Div otherPropertiesDiv = new Div(otherPropertiesLayout);

        // Datos del anunciante


        VerticalLayout mainLayout = new VerticalLayout(imagenesDiv, tituloDiv, areaDiv, descriptionDiv, otherPropertiesDiv);

        dialog.add(mainLayout);

        return dialog;
    }
}
