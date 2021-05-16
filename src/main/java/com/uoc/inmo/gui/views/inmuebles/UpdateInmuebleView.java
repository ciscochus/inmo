package com.uoc.inmo.gui.views.inmuebles;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.uoc.inmo.command.api.request.RequestFile;
import com.uoc.inmo.command.api.request.RequestInmueble;
import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.inmueble.InmuebleImages;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.entity.user.Role;
import com.uoc.inmo.query.service.InmuebleService;
import com.uoc.inmo.query.utils.ConvertionUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Route(value = "updateInmueble", layout = MainView.class)
@RouteAlias(value = "updateInmueble", layout = MainView.class)
@PageTitle("Update Inmueble")
@CssImport("./views/inmuebles/create-inmuebles-view.css")
@Secured({Role.ADMIN, Role.PROFESIONAL})
public class UpdateInmuebleView extends CreateInmuebleView implements HasUrlParameter<String>{

    private final InmuebleService inmuebleService;
    private UUID inmuebleId = null;

    public UpdateInmuebleView(@Autowired GuiInmuebleService guiInmuebleService,
                                @Autowired InmuebleService inmuebleService) {
        super(guiInmuebleService);
        this.inmuebleService = inmuebleService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String strId) {
        if (strId != null) {
            inmuebleId = UUID.fromString(strId);

            InmuebleSummary inmueble = inmuebleService.getInmuebleSummaryById(inmuebleId);
            RequestInmueble request = convertToRequest(inmueble);


            if(request != null)
                populateForm(request);
        }
    }

    private RequestInmueble convertToRequest(InmuebleSummary inmueble){
        if(inmueble == null)
            return null;

        RequestInmueble request = new RequestInmueble();

        request.setId(inmueble.getId());

        request.setTitle(inmueble.getTitle());
		request.setAddress(inmueble.getAddress());
		request.setPrice(inmueble.getPrice());
		request.setArea(inmueble.getArea());
		request.setGarage(inmueble.getGarage());
		request.setPool(inmueble.getPool());
		request.setRooms(inmueble.getRooms());
		request.setBaths(inmueble.getBaths());
		request.setDescription(inmueble.getDescription());
		request.setEmail(inmueble.getEmail());

        request.setImages(convertToRequestFileList(inmueble.getImages()));

        return request;
    }

    private List<RequestFile> convertToRequestFileList(List<InmuebleImages> images){
        List<RequestFile> requestList = new ArrayList<>();

        if(CollectionUtils.isEmpty(images))
            return requestList;

        for (InmuebleImages image : images) {
            RequestFile request = convertToRequestFile(image);
            
            if(request != null)
                requestList.add(request);
        }

        return requestList;
    }

    private RequestFile convertToRequestFile(InmuebleImages image){
        if(image == null)
            return null;

        String base64content = ConvertionUtils.toString(image.getContent());

        if(!StringUtils.hasText(base64content))
            return null;

        String content = new String(Base64.getDecoder().decode(base64content));

        if(!StringUtils.hasText(content))
            return null;

        RequestFile request = new RequestFile(image.getName(), image.getMimeType(), content);
        request.setId(image.getId());

        return request;
    }

    private void populateForm(RequestInmueble request){
        binder.readBean(request);
    }

    @Override
    public Button getSaveButton(RequestInmueble request, MultiFileMemoryBuffer buffer) {
        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.getStyle().set("marginRight", "10px");

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(request)) {

                request.setImages(readImages(buffer));
                request.setId(inmuebleId);

                if(guiInmuebleService.updateInmueble(request) == null){
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
    
}
