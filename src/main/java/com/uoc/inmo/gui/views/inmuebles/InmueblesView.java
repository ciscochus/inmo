package com.uoc.inmo.gui.views.inmuebles;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.gui.components.InmuebleCardHelper;
import com.uoc.inmo.gui.components.InmuebleDetailDialogHelper;
import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;
import com.uoc.inmo.query.service.InmuebleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "inmuebles", layout = MainView.class)
// @RouteAlias(value = "", layout = MainView.class)
@PageTitle("Inmuebles")
@CssImport("./views/inmuebles/inmuebles-view.css")
public class InmueblesView extends Div implements HasUrlParameter<String>{
    
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Grid<InmuebleSummary> grid = new Grid<>(InmuebleSummary.class, false);
    public InmuebleSummaryFilter inmuebleSummaryFilter = getFilter();

    private NumberField minPrice;
    private NumberField maxPrice;

    private NumberField minArea;
    private NumberField maxArea;

    CheckboxGroup<String> roomsCheckboxGroup;
    CheckboxGroup<String> bathsCheckboxGroup;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Button applyButton = new Button("Aplicar");
    private Button resetButton = new Button("Reset");

    // private BeanValidationBinder<InmuebleSummary> binder;

    private InmuebleSummary inmuebleSummary;

    private SubscriptionQueryResult<List<InmuebleSummary>, InmuebleSummary> fetchQueryResult;
    private InmuebleFilter filter = new InmuebleFilter(new UUID(0, 0));

    private Dialog inmuebleDetailDialog;

    private final InmuebleService inmuebleService;
    private final InmuebleDetailDialogHelper inmuebleDetailDialogHelper;


    public InmueblesView(@Autowired QueryGateway queryGateway, 
                            @Autowired InmuebleService inmuebleService,
                            @Autowired InmuebleDetailDialogHelper inmuebleDetailDialogHelper) {

        this.inmuebleService = inmuebleService;
        this.inmuebleDetailDialogHelper = inmuebleDetailDialogHelper;
        
        ConfigurableFilterDataProvider<InmuebleSummary, Void, InmuebleSummaryFilter> dataProvider = getInmuebleSummaryDataProvider(inmuebleService).withConfigurableFilter();

        dataProvider.setFilter(inmuebleSummaryFilter);

        if (fetchQueryResult != null) {
            fetchQueryResult.cancel();
            fetchQueryResult = null;
        }

        FetchInmuebleSummariesQuery fetchInmuebleSummariesQuery = new FetchInmuebleSummariesQuery(100, 100, filter);

        fetchQueryResult = queryGateway.subscriptionQuery(fetchInmuebleSummariesQuery,
                                                          ResponseTypes.multipleInstancesOf(InmuebleSummary.class),
                                                          ResponseTypes.instanceOf(InmuebleSummary.class));


        fetchQueryResult.updates().subscribe(
            inmuebleSummary -> {
                logger.trace("fetchQueryResult.updates()");
                refreshGrid();
            });
        



        addClassName("inmuebles-view");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(20);

        createGridLayout(splitLayout);
        createFilterLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        // grid.addColumn("price").setAutoWidth(true);
        grid.setDataProvider(dataProvider);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.setHeightFull();
        grid.addComponentColumn(inmueble -> createCard(inmueble));
        

        Converter converter = new Converter<String,Double>(){

            @Override
            public Result<Double> convertToModel(String value, ValueContext context) {
                return Result.ok(Double.parseDouble(value));
            }

            @Override
            public String convertToPresentation(Double value, ValueContext context) {
                return Double.toString(value);
            }
            
        };
        // binder.forField(price).withConverter(converter).bind("price");

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.inmuebleSummary == null) {
                    this.inmuebleSummary = new InmuebleSummary();
                }
                // binder.writeBean(this.inmuebleSummary);

                // inmuebleSummaryService.update(this.inmuebleSummary);
                clearForm();
                refreshGrid();
                Notification.show("InmuebleSummary details stored.");
            } catch (Exception validationException) {
                Notification.show("An exception happened while trying to store the inmuebleSummary details.");
            }
        });

    }

    public InmuebleSummaryFilter getFilter() {
        return new InmuebleSummaryFilter();
    }

    public Div createCard(InmuebleSummary inmueble) {
        return InmuebleCardHelper.createCard(inmueble, getCardAcciones(inmueble));
    }

    public Div getCardAcciones(InmuebleSummary inmueble){
        Div acciones = new Div();
        acciones.addClassNames("actions-row", "row", "justify-content-end", "align-items-end");

        //Detail
        Icon plusIcon = VaadinIcon.EYE.create();
        Button detailButton = new Button(plusIcon);

        detailButton.addClickListener(e -> navegateTo(GuiConst.PAGE_LISTADO_INMUEBLES + "/" + inmueble.getId()));

        Div detailCol = new Div(detailButton);
        detailCol.addClassName("card-button");

        acciones.add(detailCol);

        return acciones;
    }

    private void createFilterLayout(SplitLayout splitLayout) {
        Div filterLayoutDiv = new Div();
        filterLayoutDiv.setId("filter-layout");

        Div filterDiv = new Div();
        filterDiv.setId("filter");
        filterLayoutDiv.add(filterDiv);

        FormLayout formLayout = new FormLayout();

        // Price

        minPrice = new NumberField("Precio Mín");
        minPrice.setHasControls(true);

        maxPrice = new NumberField("Precio Max");
        maxPrice.setHasControls(true);

        formLayout.add(minPrice, maxPrice);

        // Area

        minArea = new NumberField("Area Mín");
        minArea.setHasControls(true);

        maxArea = new NumberField("Area Max");
        maxArea.setHasControls(true);

        formLayout.add(minArea, maxArea);

        // Rooms
        roomsCheckboxGroup = new CheckboxGroup<>();
        roomsCheckboxGroup.setLabel("Habitaciones");
        roomsCheckboxGroup.setItems("0", "1", "2", "3", "4+");

        // Baths
        bathsCheckboxGroup = new CheckboxGroup<>();
        bathsCheckboxGroup.setLabel("Baños");
        bathsCheckboxGroup.setItems("1", "2", "3+");

        formLayout.add(roomsCheckboxGroup, bathsCheckboxGroup);

        
        filterDiv.add(formLayout);
        createButtonFilterLayout(filterLayoutDiv);

        splitLayout.addToPrimary(filterLayoutDiv);
    }

    private void createButtonFilterLayout(Div filterLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);

        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClickListener(e -> filterGrid());

        resetButton.addClickListener(e -> clearForm());
        
        
        buttonLayout.add(applyButton, resetButton);

        filterLayoutDiv.add(buttonLayout);
    }

    public void filterGrid(){

        //Price
        inmuebleSummaryFilter.setMinPrice(minPrice.getValue());
        inmuebleSummaryFilter.setMaxPrice(maxPrice.getValue());

        //Area
        inmuebleSummaryFilter.setMinArea(minArea.getValue());
        inmuebleSummaryFilter.setMaxArea(maxArea.getValue());

        //Rooms
        inmuebleSummaryFilter.setRooms(roomsCheckboxGroup.getValue());

        //Baths
        inmuebleSummaryFilter.setBaths(bathsCheckboxGroup.getValue());

        refreshGrid();
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        
        wrapper.add(grid);

        splitLayout.addToSecondary(wrapper);
    }

    private void refreshGrid() {
        if(this.getUI().isPresent()){
            this.getUI().get().access(() -> {
                grid.select(null);
                grid.getDataProvider().refreshAll();
                this.getUI().get().push(); 
            });
        }
    }

    public void navegateTo(String url){
        if(this.getUI().isPresent()){
            this.getUI().get().navigate(url);
        }
    }

    private void clearForm() {
        inmuebleSummaryFilter.clear();
        
        minPrice.clear();
        // minPrice.setValue(0.0);

        maxPrice.clear();
        // maxPrice.setValue(0.0);

        minArea.clear();
        // minArea.setValue(0.0);

        maxArea.clear();
        // maxArea.setValue(0.0);

        roomsCheckboxGroup.clear();
        bathsCheckboxGroup.clear();
    }

    private DataProvider<InmuebleSummary, InmuebleSummaryFilter> getInmuebleSummaryDataProvider(InmuebleService inmuebleService){

        return DataProvider.fromFilteringCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();

                    // The number of items to load
                    int limit = query.getLimit();

                    Optional<InmuebleSummaryFilter> filter = query.getFilter();

                    List<InmuebleSummary> inmuebleSummaries = inmuebleService.fetchInmuebleSummary(offset, limit, filter);

                    return inmuebleSummaries.stream();
                },
                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> {
                    Optional<InmuebleSummaryFilter> filter = query.getFilter();
                    return inmuebleService.getInmuebleSummaryCount(filter);
                });
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String strId) {
        if (strId != null) {
            UUID inmuebleId = UUID.fromString(strId);

            inmuebleDetailDialog = inmuebleDetailDialogHelper.create(inmuebleService.getInmuebleSummaryById(inmuebleId));
            inmuebleDetailDialog.open();
        }
    }
}
