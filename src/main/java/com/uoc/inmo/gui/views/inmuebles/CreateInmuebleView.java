package com.uoc.inmo.gui.views.inmuebles;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Binder.Binding;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "createInmueble", layout = MainView.class)
@RouteAlias(value = "createInmueble", layout = MainView.class)
@PageTitle("Create Inmueble")
@CssImport("./views/inmuebles/create-inmuebles-view.css")
public class CreateInmuebleView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final GuiInmuebleService guiInmuebleService;

    public CreateInmuebleView(@Autowired GuiInmuebleService guiInmuebleService) {
        this.guiInmuebleService = guiInmuebleService;
        addCreateInmuebleForm();
    }

    public void addCreateInmuebleForm(){
        FormLayout formLayout = new FormLayout();
        formLayout.setId("create-inmueble-form-layout");

        Binder<RequestInmueble> binder = new Binder<>();
        RequestInmueble request = new RequestInmueble();
        request.setEmail(SecurityUtils.getEmailLoggedUser());

        //Type
        RadioButtonGroup<String> typeRadioGroup = new RadioButtonGroup<>();
        typeRadioGroup.setLabel("Tipo");
        typeRadioGroup.setItems(RequestInmueble.TYPE_ALQUILER, RequestInmueble.TYPE_VENTA);
        typeRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        typeRadioGroup.setValue(RequestInmueble.TYPE_ALQUILER);

        //Title
        TextField title = new TextField("Título");
        title.setPlaceholder("Título");

        //Address
        TextField address = new TextField("Dirección");
        address.setPlaceholder("Dirección");

        //Price
        TextField price = new TextField("Precio");
        price.setPlaceholder("");

        //Area
        TextField area = new TextField("Area");
        area.setPlaceholder("");

        //Rooms
        TextField rooms = new TextField("Habitaciones");
        rooms.setPlaceholder("");

        //Baths
        TextField baths = new TextField("Baños");
        baths.setPlaceholder("");

        //Garage
        Checkbox garage = new Checkbox();
        garage.setLabel("Garaje");
        garage.setValue(false);

        //Pool
        Checkbox pool = new Checkbox();
        pool.setLabel("Piscina");
        pool.setValue(false);

        //Description
        TextArea description = new TextArea("Descripcion");
        description.setPlaceholder("Descripcion ...");

        formLayout.add(typeRadioGroup, title, address, price, area, rooms, baths, garage, pool, description);

        // Button bar
        NativeButton save = new NativeButton("Save");
        NativeButton reset = new NativeButton("Reset");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        // Info label
        Label infoLabel = new Label();

        //Binders
        Binding<RequestInmueble, String> typeBinding = binder.forField(typeRadioGroup)
            .withNullRepresentation(RequestInmueble.TYPE_ALQUILER)
            .bind(RequestInmueble::getType, RequestInmueble::setType);

        Binding<RequestInmueble, String> titleBinding = binder.forField(title)
            .withNullRepresentation("")
            .bind(RequestInmueble::getTitle, RequestInmueble::setTitle);

        Binding<RequestInmueble, String> addressBinding = binder.forField(address)
            .withNullRepresentation("")
            .bind(RequestInmueble::getAddress, RequestInmueble::setAddress);

        Binding<RequestInmueble, Double> priceBinding = binder.forField(price)
            .withConverter(new StringToDoubleConverter(0.0, "Precio incorrecto"))
            .bind(RequestInmueble::getPrice, RequestInmueble::setPrice);

        Binding<RequestInmueble, Double> areaBinding = binder.forField(area)
            .withConverter(new StringToDoubleConverter(0.0,"Area incorrecta"))
            .bind(RequestInmueble::getArea, RequestInmueble::setArea);
        
        Binding<RequestInmueble, Integer> roomsBinding = binder.forField(rooms)
            .withConverter(new StringToIntegerConverter(0, "Número de habitaciones incorrecto"))
            .bind(RequestInmueble::getRooms, RequestInmueble::setRooms);

        Binding<RequestInmueble, Integer> bathsBinding = binder.forField(baths)
            .withConverter(new StringToIntegerConverter(0, "Número de baños incorrecto"))
            .bind(RequestInmueble::getBaths, RequestInmueble::setBaths);

        Binding<RequestInmueble, String> descriptionBinding = binder.forField(description)
            .withNullRepresentation("")
            .bind(RequestInmueble::getDescription, RequestInmueble::setDescription);

        Binding<RequestInmueble, Boolean> garageBinding = binder.forField(garage)
            .withNullRepresentation(false)
            .bind(RequestInmueble::getGarage, RequestInmueble::setGarage);

        Binding<RequestInmueble, Boolean> poolBinding = binder.forField(pool)
            .withNullRepresentation(false)
            .bind(RequestInmueble::getPool, RequestInmueble::setPool);

        // Click listeners for the buttons
        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(request)) {
                if(guiInmuebleService.createInmueble(request) == null){
                    infoLabel.setText("Error!");
                } else {
                    infoLabel.setText("Saved bean values: " + request);
                }
            } else {
                BinderValidationStatus<RequestInmueble> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
        });

        add(formLayout, actions, infoLabel);
    }

    
}
