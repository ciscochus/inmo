package com.uoc.inmo.gui.views.inmuebles;

import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.gui.security.SecurityUtils;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.entity.user.Role;
import com.uoc.inmo.query.service.InmuebleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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

    public MisInmueblesView(@Autowired QueryGateway queryGateway, @Autowired InmuebleService inmuebleService) {
        super(queryGateway, inmuebleService);
    }

    @Override
    public HorizontalLayout getCardAcciones() {
        HorizontalLayout acciones = super.getCardAcciones();

        //Update
        Icon editIcon = VaadinIcon.EDIT.create();
        Button updateButton = new Button(editIcon);

        acciones.add(updateButton);

        //Delete
        Icon deleteIcon = VaadinIcon.TRASH.create();
        Button deleteButton = new Button(deleteIcon);

        acciones.add(deleteButton);

        return acciones;
    }

    @Override
    public InmuebleSummaryFilter getFilter() {
        InmuebleSummaryFilter filter = super.getFilter();
        filter.setEmail(SecurityUtils.getEmailLoggedUser());

        return filter;
    }

    

    

    
    
}
