package com.uoc.inmo.gui.views.inmuebles;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.uoc.inmo.gui.data.filters.InmuebleSummaryFilter;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;
import com.uoc.inmo.query.service.InmuebleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "inmuebles", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Inmuebles")
@CssImport("./views/inmuebles/inmuebles-view.css")
public class InmueblesView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Grid<InmuebleSummary> grid = new Grid<>(InmuebleSummary.class, false);
    private InmuebleSummaryFilter inmuebleSummaryFilter = new InmuebleSummaryFilter();

    private TextField price;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Button applyButton = new Button("Aplicar");

    // private BeanValidationBinder<InmuebleSummary> binder;

    private InmuebleSummary inmuebleSummary;

    private SubscriptionQueryResult<List<InmuebleSummary>, InmuebleSummary> fetchQueryResult;
    private InmuebleFilter filter = new InmuebleFilter(new UUID(0, 0));


    public InmueblesView(@Autowired QueryGateway queryGateway, @Autowired InmuebleService inmuebleService) {
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

    private HorizontalLayout createCard(InmuebleSummary inmueble) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");
        card.setWidthFull();

        //Image-zone
        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.addClassName("imageLayout");

        //Image-zone
        VerticalLayout propertiesLayout = new VerticalLayout();
        propertiesLayout.addClassName("propertiesLayout");

        Span superficie = new Span(inmueble.getArea()+" m2");
        superficie.addClassName("superficie");

        Span rooms = new Span(inmueble.getRooms()+" hab.");
        rooms.addClassName("rooms");

        Span baths = new Span(inmueble.getBaths()+" baños");
        baths.addClassName("baths");

        propertiesLayout.add(superficie, rooms, baths);

        //Center-zone
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");

        Span title = new Span(inmueble.getTitle());
        title.addClassName("title");

        Span address = new Span(inmueble.getAddress());
        address.addClassName("address");

        description.add(title, address);


        //Right-zone
        VerticalLayout priceLayout = new VerticalLayout();
        priceLayout.addClassName("priceLayout");
        priceLayout.setAlignItems(Alignment.END);

        Span price = new Span(inmueble.getPrice()+" €");
        price.addClassName("price");

        Icon plusIcon = VaadinIcon.PLUS_CIRCLE_O.create();
        Button detailButton = new Button(plusIcon);

        priceLayout.add(price,detailButton);


        card.add(imageLayout, propertiesLayout, description, priceLayout);

        return card;
    }

    private void createFilterLayout(SplitLayout splitLayout) {
        Div filterLayoutDiv = new Div();
        filterLayoutDiv.setId("filter-layout");

        Div filterDiv = new Div();
        filterDiv.setId("filter");
        filterLayoutDiv.add(filterDiv);

        FormLayout formLayout = new FormLayout();
        price = new TextField("price");
        Component[] fields = new Component[]{price};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
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
        
        
        buttonLayout.add(applyButton);

        filterLayoutDiv.add(buttonLayout);
    }

    public void filterGrid(){
        inmuebleSummaryFilter.setPrice(price.getValue());
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

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(InmuebleSummary value) {
        this.inmuebleSummary = value;
        // binder.readBean(this.inmuebleSummary);

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
}
