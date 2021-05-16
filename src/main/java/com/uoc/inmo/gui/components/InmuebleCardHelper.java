package com.uoc.inmo.gui.components;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.Base64;
import java.util.List;

import com.uoc.inmo.query.entity.inmueble.InmuebleImages;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.utils.ConvertionUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.StreamResource;

import org.springframework.util.CollectionUtils;

public class InmuebleCardHelper {
    

    public static Div createCard(InmuebleSummary inmueble, Div acciones){

        Div card = new Div();
        card.addClassNames("container", "card");

            Div row = new Div();
            row.addClassName("row");

                Div imageColumn = new Div();
                imageColumn.addClassNames("col-2","image-col");

                imageColumn.add(getThumbnail(inmueble));

                Div propertiesColumn = new Div();
                propertiesColumn.addClassNames("col-3","properties-col");

                    Div areaRow = new Div();
                    areaRow.addClassNames("row", "area-row");
                    areaRow.add(new Label("Superficie:"), new Span(inmueble.getArea()+" m2"));

                    Div roomsRow = new Div();
                    roomsRow.addClassNames("row", "rooms-row");
                    roomsRow.add(new Label("Habitaciones:"), new Span(inmueble.getRooms()+""));

                    Div bathsRow = new Div();
                    bathsRow.addClassNames("row", "baths-row");
                    bathsRow.add(new Label("Baños:"), new Span(inmueble.getBaths()+""));

                propertiesColumn.add(areaRow, roomsRow, bathsRow);

                Div addressColumn = new Div();
                addressColumn.addClassNames("col","address-col");

                    Div titleRow = new Div();
                    titleRow.addClassNames("row", "title-row");
                    titleRow.add(new Span(inmueble.getTitle()));

                    Div addressRow = new Div();
                    addressRow.addClassNames("row", "address-row");
                    addressRow.add(new Span(inmueble.getAddress()));

                    Div descriptionRow = new Div();
                    descriptionRow.addClassNames("row", "description-row");
                    descriptionRow.add(new Span(inmueble.getDescription()));

                    addressColumn.add(titleRow, addressRow, descriptionRow);

                Div actionsColumn = new Div();
                actionsColumn.addClassNames("col-2","actions-col");

                    Div priceRow = new Div();
                    priceRow.addClassNames("price-row");
                    priceRow.add(new Span(inmueble.getPrice()+" €"));

                    actionsColumn.add(priceRow, acciones);

            row.add(imageColumn, propertiesColumn, addressColumn, actionsColumn);
        
        card.add(row);
        
        return card;
    }

    private static Div getThumbnail(InmuebleSummary inmueble){

        List<InmuebleImages> images = inmueble.getImages();

        Div nofoto = new Div();
        nofoto.addClassName("no-foto");
        nofoto.add(new Span("Sin fotos"));

        if(CollectionUtils.isEmpty(images))
            return nofoto;

        InmuebleImages image = images.get(0);

        Image img = convertToImageComponent(image);

        if(img == null)
            return nofoto;
        

        Div thumbnail = new Div();
        thumbnail.addClassName("thumbnail");

        img.addClassName("img-thumbnail");

        thumbnail.add(img);
        
        return thumbnail;
    }

    public static Image convertToImageComponent(InmuebleImages image){

        String name = image.getName();
        Blob contentBlob = image.getContent();
        String base64content = ConvertionUtils.toString(contentBlob);

        if(base64content == null)
            return null;

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64content);
            StreamResource resource = new StreamResource(name, () -> new ByteArrayInputStream(imageBytes));
            return new Image(resource, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
