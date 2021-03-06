package com.uoc.inmo.gui.views.inmuebles;

import java.util.UUID;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.components.InmuebleDetailDialogHelper;
import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.entity.user.Role;
import com.uoc.inmo.query.service.InmuebleService;
import com.uoc.inmo.query.service.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "misInmuebles", layout = MainView.class)
@RouteAlias(value = "misInmuebles", layout = MainView.class)
@PageTitle("Mis Inmuebles")
@CssImport("./views/inmuebles/inmuebles-view.css")
@Secured({Role.ADMIN, Role.PROFESIONAL})
public class MisInmueblesView extends InmueblesView {

    private final GuiInmuebleService guiInmuebleService;

    public MisInmueblesView(@Autowired QueryGateway queryGateway, 
                            @Autowired InmuebleService inmuebleService, 
                            @Autowired InmuebleDetailDialogHelper inmuebleDetailDialogHelper,
                            @Autowired UserService userService,
                            @Autowired GuiInmuebleService guiInmuebleService) {
        super(queryGateway, inmuebleService, inmuebleDetailDialogHelper, userService);
        this.guiInmuebleService = guiInmuebleService;
        
    }

    @Override
    public Div getCardAcciones(InmuebleSummary inmueble) {
        
        Div acciones = super.getCardAcciones(inmueble);

        //Update
        Icon editIcon = VaadinIcon.EDIT.create();
        Button updateButton = new Button(editIcon);

        updateButton.addClickListener(e -> navegateTo(GuiConst.PAGE_UPDATE_INMUEBLE + "/" + inmueble.getId()));

        Div updateCol = new Div(updateButton);
        updateCol.addClassName("card-button");

        acciones.add(updateCol);

        //Delete
        Icon deleteIcon = VaadinIcon.TRASH.create();
        Button deleteButton = new Button(deleteIcon);
        deleteButton.addClickListener(e -> deleteConfirmDialog(inmueble.getId()).open());

        Div deleteCol = new Div(deleteButton);
        deleteCol.addClassName("card-button");

        acciones.add(deleteCol);

        return acciones;
    }

    @Override
    public InmuebleSummaryFilter getFilter() {
        InmuebleSummaryFilter filter = super.getFilter();
        filter.setEmail(SecurityUtils.getEmailLoggedUser());

        return filter;
    }


    public Dialog deleteConfirmDialog(UUID inmuebleId){
        Dialog dialog = new Dialog();

        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Aceptar", event -> {
            deleteInmueble(inmuebleId);
            dialog.close();
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", event -> {
            dialog.close();
        });

        // Cancel action on ESC press
        Shortcuts.addShortcutListener(dialog, () -> {
            dialog.close();
        }, Key.ESCAPE);

        Div buttons = new Div(confirmButton, cancelButton);
        buttons.setClassName("confirm-buttons");

        Div dialogContentDiv = new Div(new Text("Esta acci??n no puede deshacerse. ??Desea continuar?"), buttons);
        dialogContentDiv.setClassName("confirmDialog");

        dialog.add(dialogContentDiv);

        return dialog;
    }

    public void deleteInmueble(UUID idInmueble){
        guiInmuebleService.deleteInmueble(idInmueble);
        new Notification(GuiConst.NOTIFICATION_SAVE_OK, GuiConst.NOTIFICATION_TIME, GuiConst.NOTIFICACION_POSITION).open();
    }

    

    
    
}
