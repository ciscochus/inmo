package com.uoc.inmo.gui.views.inmuebles;

import java.util.UUID;

import com.uoc.inmo.gui.components.InmuebleDetailDialogHelper;
import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.service.GuiInmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.entity.user.Role;
import com.uoc.inmo.query.service.InmuebleService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
                            @Autowired GuiInmuebleService guiInmuebleService) {
        super(queryGateway, inmuebleService, inmuebleDetailDialogHelper);
        this.guiInmuebleService = guiInmuebleService;
        
    }

    @Override
    public Div getCardAcciones(InmuebleSummary inmueble) {
        
        Div acciones = super.getCardAcciones(inmueble);

        //Update
        Icon editIcon = VaadinIcon.EDIT.create();
        Button updateButton = new Button(editIcon);

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
        dialog.add(new Text("Esta acción no puede deshacerse. ¿Desea continuar?"));
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        Button confirmButton = new Button("Aceptar", event -> {
            deleteInmueble(inmuebleId);
            dialog.close();
        });
        Button cancelButton = new Button("Cancelar", event -> {
            dialog.close();
        });
        // Cancel action on ESC press
        Shortcuts.addShortcutListener(dialog, () -> {
            dialog.close();
        }, Key.ESCAPE);

        dialog.add(new Div(confirmButton, cancelButton));

        return dialog;
    }

    public void deleteInmueble(UUID idInmueble){
        guiInmuebleService.deleteInmueble(idInmueble);
    }

    

    
    
}
