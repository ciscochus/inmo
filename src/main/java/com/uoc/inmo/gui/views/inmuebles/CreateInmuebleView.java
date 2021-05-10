package com.uoc.inmo.gui.views.inmuebles;

import com.uoc.inmo.gui.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "createInmueble", layout = MainView.class)
@RouteAlias(value = "createInmueble", layout = MainView.class)
@PageTitle("Create Inmueble")
@CssImport("./views/inmuebles/create-inmuebles-view.css")
public class CreateInmuebleView extends Div {
    
}
