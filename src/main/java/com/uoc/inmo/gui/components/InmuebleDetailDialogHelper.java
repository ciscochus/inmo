package com.uoc.inmo.gui.components;

import java.util.List;
import java.util.UUID;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.util.DateUtils;
import com.uoc.inmo.gui.util.NumberUtils;
import com.uoc.inmo.query.api.response.ResponsePrice;
import com.uoc.inmo.query.entity.inmueble.InmuebleImages;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.entity.user.Inmobiliaria;
import com.uoc.inmo.query.entity.user.User;
import com.uoc.inmo.query.utils.ConvertionUtils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.vaadin.pekkam.Canvas;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import lombok.Data;

@Service
@Data
public class InmuebleDetailDialogHelper {
    
    @Autowired GuiInmuebleService guiInmuebleService;

    public Dialog create(InmuebleSummary inmueble, User user){
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setId("inmueble-detail-dialog");

        Div dialogContainerDiv = new Div();
        dialogContainerDiv.addClassNames("dialog-container", "container");

        if(inmueble == null)
            return dialog;

        //Imagenes
        Div imagenesDiv = new Div();
        imagenesDiv.setId("imagenes-div");

        Div carousel = getImagesCarousel(inmueble);
        if(carousel != null)
            imagenesDiv.add(carousel);

        dialogContainerDiv.add(imagenesDiv);
        // barra separadora


        if(SecurityUtils.isUserLoggedIn()){
            // Historico precios

            Button openChartButton = new Button(VaadinIcon.LINE_CHART.create());
            if(inmueble.getPriceChanged()){
                Div priceChartDiv = getPriceChart(inmueble);
                dialogContainerDiv.add(priceChartDiv);
                openChartButton.setClassName("open-chart-button");
                openChartButton.addClickListener(e -> {
                    UI.getCurrent().getPage().executeJs("togglePriceChart()");
                });
            } else {
                openChartButton.setEnabled(false);
            }

            Checkbox subscribeChecbox = new Checkbox("Avísame si cambia de precio");
            subscribeChecbox.setId("subscribeSwitch");

            Boolean isSubscribed = guiInmuebleService.checkInmuebleSubscription(inmueble.getId(), SecurityUtils.getEmailLoggedUser());

            subscribeChecbox.setValue(isSubscribed);
            subscribeChecbox.addValueChangeListener(e -> subscribeInmueble(inmueble, e.getValue()));

            Div controlBar = new Div(subscribeChecbox, openChartButton);

            controlBar.addClassName("controlBar");

            dialogContainerDiv.add(controlBar);
        }

        // Titulo

        Div tituloRow = new Div();
        tituloRow.addClassName("row");
        tituloRow.setId("titulo-div");

        Span titulo = new Span(inmueble.getTitle());
        titulo.setClassName("inmueble-title");

        Div tituloCol = new Div(titulo);
        tituloCol.addClassName("col-6");

        Span address = new Span(inmueble.getAddress());
        address.setClassName("inmueble-address");

        Div addressCol = new Div(address);
        addressCol.addClassNames("col", "address-col");

        tituloRow.add(tituloCol, addressCol);

        // Superficie - habitaciones - precio

        Div areaRow = new Div();
        areaRow.addClassName("row");
        areaRow.setId("area-div");

        Label areaLabel = new Label("Superficie: ");
        areaLabel.addClassNames("label", "area-label");

        Span areaValue = new Span(Double.toString(inmueble.getArea()));
        areaValue.addClassNames("area-value");

        Div areaCol = new Div(areaLabel, areaValue);
        areaCol.addClassName("col-3");

        Label roomsLabel = new Label("Habitaciones: ");
        roomsLabel.addClassNames("label", "rooms-label");

        Span roomsValue = new Span(Integer.toString(inmueble.getRooms()));
        roomsValue.addClassNames("rooms-value");

        Div roomsCol = new Div(roomsLabel, roomsValue);
        roomsCol.addClassName("col-2");

        Label bathsLabel = new Label("Baños: ");
        bathsLabel.addClassNames("label", "baths-label");

        Span bathsValue = new Span(Integer.toString(inmueble.getBaths()));
        bathsValue.addClassNames("baths-value");

        Div bathsCol = new Div(bathsLabel, bathsValue);
        bathsCol.addClassName("col-2");

        Span price = new Span(NumberUtils.getFormatPrice(inmueble.getPrice())+" €");
        price.setClassName("inmueble-price");

        Div priceCol = new Div(price);
        priceCol.addClassNames("col", "price-col");

        areaRow.add(areaCol, roomsCol, bathsCol, priceCol);
        areaRow.setId("area-div");

        // Descripcion
        Div descriptionCol = new Div(new Text(inmueble.getDescription() != null ? inmueble.getDescription() : ""));
        descriptionCol.addClassName("col");

        Div descriptionRow = new Div(descriptionCol);
        descriptionRow.setId("inmueble-description");
        descriptionRow.addClassName("row");

        // barra separadora

        // Otras características

        Checkbox garageCheck = new Checkbox("Garaje", inmueble.getGarage() != null && inmueble.getGarage());
        garageCheck.setReadOnly(true);

        Div garageCheckCol = new Div(garageCheck);
        garageCheckCol.addClassName("col-2");

        Checkbox poolCheck = new Checkbox("Piscina", inmueble.getPool() != null && inmueble.getPool());
        poolCheck.setReadOnly(true);

        Div poolCheckCol = new Div(poolCheck);
        poolCheckCol.addClassName("col");

        Div otherPropertiesRow = new Div(garageCheckCol, poolCheckCol);
        otherPropertiesRow.addClassName("row");
        otherPropertiesRow.setId("otherProperties-div");

        //Dates

        Label createdLabel = new Label("Publicado: ");
        createdLabel.addClassNames("label", "created-label");

        Span created = new Span(DateUtils.formatDate(inmueble.getCreated()));
        created.addClassNames("date","created-date");

        Div createdCol = new Div(createdLabel, created);
        createdCol.addClassNames("col-6","created-col");

        Label updatedLabel = new Label("Actualizado: ");
        updatedLabel.addClassNames("label", "updated-label");

        Span updated = new Span(DateUtils.formatDate(inmueble.getUpdated()));
        updated.addClassNames("date","updated-date");

        Div updatedCol = new Div(updatedLabel, updated);
        updatedCol.addClassNames("col-6","updated-col");

        Div datesRow = new Div(createdCol, updatedCol);
        datesRow.addClassName("row");
        datesRow.setId("dates-div");

        // Datos del anunciante

        Div inmobiliariaRow = getInmobiliariaInfo(inmueble, user);

        dialogContainerDiv.add(tituloRow, areaRow, descriptionRow, otherPropertiesRow, datesRow, inmobiliariaRow);

        dialog.add(dialogContainerDiv);

        return dialog;
    }

    private void subscribeInmueble(InmuebleSummary inmueble, Boolean value) {
        String email = SecurityUtils.getEmailLoggedUser();
        UUID idInmueble = inmueble.getId();

        Boolean response = false;
        if(value){
           response = guiInmuebleService.addInmuebleSubscription(idInmueble, email);
        } else {
            response = guiInmuebleService.deleteInmuebleSubscription(idInmueble, email);
        }

        String mensaje = "Se ha producido un error";
        if(response){
            mensaje = value ? "Subscripción activada" : "Subscripción desactivada";
        }
        
        new Notification(mensaje, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
    }

    Div getImagesCarousel(InmuebleSummary inmueble){

        Div nofoto = new Div();
        nofoto.addClassName("no-foto");
        nofoto.add(new Span("Sin fotos"));

        List<InmuebleImages> images = inmueble.getImages();

        if(CollectionUtils.isEmpty(images))
            return nofoto;

        Div carousel = new Div();
        carousel.addClassNames("carousel", "slide");
        carousel.getElement().setAttribute("data-ride", "carousel");
        carousel.setId("detail-carousel");

        Div inner = new Div();
        inner.addClassName("carousel-inner");

        boolean firstElement = true;

        for (InmuebleImages inmuebleImage : images) {
            
            Div item = getCarouselItem(inmuebleImage, firstElement);

            firstElement = false;

            if(item != null)
                inner.add(item);
        }

        Anchor prev = new Anchor("#detail-carousel");
        prev.addClassName("carousel-control-prev");
        prev.getElement().setAttribute("role","button");
        prev.getElement().setAttribute("data-slide","prev");

        Span controlPrev = new Span();
        controlPrev.addClassName("carousel-control-prev-icon");
        controlPrev.getElement().setAttribute("aria-hidden", "true");

        Span srOnlyPrev = new Span("Anterior");
        srOnlyPrev.addClassName("sr-only");

        prev.add(controlPrev, srOnlyPrev);

        Anchor next = new Anchor("#detail-carousel");
        next.addClassName("carousel-control-next");
        next.getElement().setAttribute("role","button");
        next.getElement().setAttribute("data-slide","next");

        Span controlNext = new Span();
        controlNext.addClassName("carousel-control-next-icon");
        controlNext.getElement().setAttribute("aria-hidden", "true");

        Span srOnlyNext= new Span("Siguiente");
        srOnlyNext.addClassName("sr-only");

        next.add(controlNext, srOnlyNext);

        carousel.add(inner, prev, next);

        return carousel;
    }

    private Div getCarouselItem(InmuebleImages inmuebleImage, boolean active){

        Div carouselItem = new Div();
        carouselItem.addClassName("carousel-item");

        if(active)
            carouselItem.addClassName("active");

        Image img = InmuebleCardHelper.convertToImageComponent(inmuebleImage);

        if(img == null)
            return null;

        // img.addClassNames("d-block", "w-100");

        carouselItem.add(img);

        return carouselItem;
    }

    Div getInmobiliariaInfo(InmuebleSummary inmueble, User user){

        Div inmobiliariaInfoDiv = new Div();
        inmobiliariaInfoDiv.setId("inmobiliaria-info-container");
        inmobiliariaInfoDiv.addClassName("container");

        if(inmueble == null || user == null || user.getInmobiliaria() == null)
            return inmobiliariaInfoDiv;

        Inmobiliaria inmobiliaria = user.getInmobiliaria();

        Div titleCol = new Div(new Span("Datos del anunciante"));
        titleCol.addClassNames("col", "inmobiliaria-title-col");

        Div titleRow = new Div(titleCol);
        titleRow.addClassName("row");

        Div mainDiv = new Div();
        mainDiv.addClassName("row");

        Div left = new Div();
        left.addClassName("col-3");

        Div rigth = new Div();
        rigth.addClassName("col-9");

        Div phoneCol = new Div(new Span(inmobiliaria.getPhone()));
        phoneCol.addClassName("col");

        Div phoneRow = new Div(phoneCol);
        phoneRow.addClassName("row");

        Div mailCol = new Div(new Span(user.getEmail()));
        mailCol.addClassName("col");

        Div mailRow = new Div(mailCol);
        mailRow.addClassName("row");

        Div webCol = new Div(new Span(inmobiliaria.getWeb()));
        webCol.addClassName("col");

        Div webRow = new Div(webCol);
        webRow.addClassName("row");

        Div addressCol = new Div(new Span(inmobiliaria.getAddress()));
        addressCol.addClassName("col");

        Div addressRow = new Div(addressCol);
        addressRow.addClassName("row");

        left.add(phoneRow, mailRow, webRow, addressRow);

        Div descriptionCol = new Div(new Paragraph(inmobiliaria.getDescription()));
        descriptionCol.addClassName("col");

        rigth.add(descriptionCol);

        mainDiv.add(left,rigth);
        inmobiliariaInfoDiv.add(titleRow, mainDiv);

        return inmobiliariaInfoDiv;
    }

    Div getPriceChart(InmuebleSummary inmueble){
        Div priceChartDiv = new Div();
        priceChartDiv.setId("priceChart");
        priceChartDiv.addClassName("d-none");

        Canvas canvas = new Canvas(300, 100);
        canvas.setId("canvas-price");

        priceChartDiv.add(canvas);

        Span data = new Span(getInmueblePriceHistory(inmueble.getId()));
        data.addClassName("d-none");
        data.setId("priceChart-data");

        priceChartDiv.add(data);

        return priceChartDiv;
    }

    String getInmueblePriceHistory(UUID inmuebleId){
        List<ResponsePrice> history = guiInmuebleService.getInmueblePriceHistory(inmuebleId);

        if(CollectionUtils.isEmpty(history))
            return "";

        JsonArray array = Json.createArray();

        for (ResponsePrice responsePrice : history) {
            array.set(array.length(), getHistoryItem(responsePrice));
        }
        
        return array.toJson();
    }

    JsonObject getHistoryItem(ResponsePrice responsePrice){

        JsonObject item = Json.createObject();
        
        item.put("x", ConvertionUtils.dateToString(responsePrice.getCreated()));
        item.put("y", responsePrice.getPrice());

        return item;
    }
}
