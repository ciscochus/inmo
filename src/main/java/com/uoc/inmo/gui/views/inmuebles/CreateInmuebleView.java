package com.uoc.inmo.gui.views.inmuebles;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.uoc.inmo.command.api.request.RequestFile;
import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.user.Role;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Route(value = "createInmueble", layout = MainView.class)
@RouteAlias(value = "createInmueble", layout = MainView.class)
@PageTitle("Create Inmueble")
@CssImport("./views/inmuebles/create-inmuebles-view.css")
@Secured({Role.ADMIN, Role.PROFESIONAL})
public class CreateInmuebleView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Binder<RequestInmueble> binder;
    public Label infoLabel = new Label();

    protected final GuiInmuebleService guiInmuebleService;

    public CreateInmuebleView(@Autowired GuiInmuebleService guiInmuebleService) {
        this.guiInmuebleService = guiInmuebleService;
        add(addCreateInmuebleForm());
    }

    public Div addCreateInmuebleForm(){
        Div formDiv = new Div();
        formDiv.setId("create-inmueble-container");
        formDiv.addClassName("container");

        Div formLayout = new Div();
        formLayout.setId("create-inmueble-form-layout");

        binder = new Binder<>();

        //Type
        Label typeLabel = new Label("Tipo");
        typeLabel.addClassNames("col-sm-1", "col-form-label");

        RadioButtonGroup<String> typeRadioGroup = new RadioButtonGroup<>();
        typeRadioGroup.setItems(RequestInmueble.TYPE_ALQUILER, RequestInmueble.TYPE_VENTA);
        typeRadioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        typeRadioGroup.setValue(RequestInmueble.TYPE_ALQUILER);

        Div typeRadioGroupDiv = new Div(typeRadioGroup);
        typeRadioGroupDiv.addClassNames("col-sm-10");

        Div typeDiv = new Div(typeLabel,typeRadioGroupDiv);
        typeDiv.addClassNames("form-group", "row");

        //Title
        Label titleLabel = new Label("Título");
        titleLabel.addClassNames("col-sm-1", "col-form-label");

        TextField title = new TextField();
        title.setPlaceholder("Título");
        title.addClassName("full-width");

        Div titleInputDiv = new Div(title);
        titleInputDiv.addClassNames("col-sm-7");

        Div titleDiv = new Div(titleLabel,titleInputDiv);
        titleDiv.addClassNames("form-group", "row");

        //Address
        Label addressLabel = new Label("Dirección");
        addressLabel.addClassNames("col-sm-1", "col-form-label");

        TextField address = new TextField();
        address.setPlaceholder("Dirección");
        address.addClassName("full-width");

        Div addressInputDiv = new Div(address);
        addressInputDiv.addClassNames("col-sm-7");

        Div addressDiv = new Div(addressLabel,addressInputDiv);
        addressDiv.addClassNames("form-group", "row");

        //Price
        Label priceLabel = new Label("Precio");
        priceLabel.addClassNames("col-sm-1", "col-form-label");

        TextField price = new TextField();
        price.setPlaceholder("");

        Div priceInputDiv = new Div(price);
        priceInputDiv.addClassNames("col-sm-3");

        //Area
        Label areaLabel = new Label("Superficie");
        areaLabel.addClassNames("col-sm-1", "col-form-label");

        TextField area = new TextField();
        area.setPlaceholder("");

        Div areaInputDiv = new Div(area);
        areaInputDiv.addClassNames("col-sm-7");

        Div priceAreaDiv = new Div(priceLabel,priceInputDiv, areaLabel,areaInputDiv);
        priceAreaDiv.addClassNames("form-group", "row");
        priceAreaDiv.setId("priceAreaDiv");

        //Rooms
        Label roomsLabel = new Label("Habitaciones");
        roomsLabel.addClassNames("col-sm-1", "col-form-label");

        TextField rooms = new TextField();
        rooms.setPlaceholder("");

        Div roomsInputDiv = new Div(rooms);
        roomsInputDiv.addClassNames("col-sm-3");

        //Baths
        Label bathsLabel = new Label("Baños");
        bathsLabel.addClassNames("col-sm-1", "col-form-label");

        TextField baths = new TextField();
        baths.setPlaceholder("");

        Div bathsInputDiv = new Div(baths);
        bathsInputDiv.addClassNames("col-sm-7");

        Div roomsBathsDiv = new Div(roomsLabel, roomsInputDiv, bathsLabel, bathsInputDiv);
        roomsBathsDiv.addClassNames("form-group", "row");
        roomsBathsDiv.setId("roomsBathsDiv");

        //Garage
        Label garageLabel = new Label("Garaje");
        garageLabel.addClassNames("col-sm-1", "col-form-label");

        Checkbox garage = new Checkbox();
        garage.setValue(false);

        Div garageInputDiv = new Div(garage);
        garageInputDiv.addClassNames("col-sm-3");

        //Pool
        Label poolLabel = new Label("Piscina");
        poolLabel.addClassNames("col-sm-1", "col-form-label");

        Checkbox pool = new Checkbox();
        pool.setValue(false);

        Div poolInputDiv = new Div(pool);
        poolInputDiv.addClassNames("col-sm-7");

        Div propertiesDiv = new Div(garageLabel,garageInputDiv,poolLabel,poolInputDiv);
        propertiesDiv.addClassNames("form-group", "row");

        //Description
        Label descriptionLabel = new Label("Descripción");
        descriptionLabel.addClassNames("col-sm-1", "col-form-label");

        TextArea description = new TextArea();
        description.setPlaceholder("Descripcion ...");
        description.addClassName("full-width");

        Div descriptionInputDiv = new Div(description);
        descriptionInputDiv.addClassNames("col-sm-7");

        Div descriptionDiv = new Div(descriptionLabel,descriptionInputDiv);
        descriptionDiv.addClassNames("form-group", "row");

        //Images
        Label uploadLabel = new Label("Imágenes");
        uploadLabel.addClassNames("col-sm-1", "col-form-label");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addClassName("full-width");

        upload.setMaxFiles(GuiConst.MAX_IMAGES_UPLOAD);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            infoLabel.setText("MimeType: "+event.getMIMEType()+", ");
            infoLabel.add("FileName: "+event.getFileName());
        });

        Div uploadInputDiv = new Div(upload);
        uploadInputDiv.addClassNames("col-sm-7");

        Div uploadDiv = new Div(uploadLabel,uploadInputDiv);
        uploadDiv.addClassNames("form-group", "row");

        formLayout.add(typeDiv, titleDiv, addressDiv, priceAreaDiv, roomsBathsDiv, propertiesDiv, descriptionDiv, uploadDiv);

        HorizontalLayout actions = getAcciones(buffer);

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

        formDiv.add(formLayout, actions, infoLabel);

        return formDiv;
    }

    public HorizontalLayout getAcciones(MultiFileMemoryBuffer buffer){
        RequestInmueble request = new RequestInmueble();
        request.setEmail(SecurityUtils.getEmailLoggedUser());

        // Button bar
        Button save = getSaveButton(request, buffer);

        Button reset = new Button("Reset");
        
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
        });

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        
        return actions;
    }

    public Button getSaveButton(RequestInmueble request, MultiFileMemoryBuffer buffer) {
        
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.getStyle().set("marginRight", "10px");

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(request)) {

                request.setImages(readImages(buffer));
                if(guiInmuebleService.createInmueble(request) == null){
                    infoLabel.setText("Error!");
                    new Notification(GuiConst.NOTIFICATION_SAVE_ERROR, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
                } else {
                    infoLabel.setText("Saved bean values: " + request);
                    new Notification(GuiConst.NOTIFICATION_SAVE_OK, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
                    navegateTo(GuiConst.PAGE_LISTADO_MIS_INMUEBLES);
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

        return save;
    }


    protected List<RequestFile> readImages(MultiFileMemoryBuffer buffer) {

        Set<String> files = buffer.getFiles();

        if(CollectionUtils.isEmpty(files))
            return new ArrayList<>();

        List<RequestFile> images = new ArrayList<>();

        for(String fileName : buffer.getFiles()) {

            RequestFile requestImage = readImage(fileName, buffer);
            if(requestImage != null)
                images.add(requestImage);
        }

        return images;
    }

    private RequestFile readImage(String fileName, MultiFileMemoryBuffer buffer){

        try {
            InputStream inputStream = buffer.getInputStream(fileName);
            
            if(inputStream != null){
                byte[] byteContent = buffer.getInputStream(fileName).readAllBytes();
                String base64content = Base64.getEncoder().encodeToString(byteContent);

                String mimeType = buffer.getFileData(fileName).getMimeType();

                if(StringUtils.hasText(base64content) && StringUtils.hasText(mimeType)){
                    return new RequestFile(fileName, mimeType, base64content);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public void navegateTo(String url){
        if(this.getUI().isPresent()){
            this.getUI().get().navigate(url);
        }
    }
    
}
