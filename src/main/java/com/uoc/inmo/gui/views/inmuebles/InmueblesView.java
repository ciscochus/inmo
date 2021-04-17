package com.uoc.inmo.gui.views.inmuebles;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.UUID;

import com.uoc.inmo.gui.data.provider.InmuebleSummaryDataProvider;
import com.uoc.inmo.gui.data.service.InmuebleService;
import com.uoc.inmo.gui.views.main.MainView;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "hello", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Inmuebles")
@CssImport("./views/inmuebles/inmuebles-view.css")
public class InmueblesView extends Div {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Grid<InmuebleSummary> grid = new Grid<>(InmuebleSummary.class, false);

    private TextField price;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    // private BeanValidationBinder<InmuebleSummary> binder;

    private InmuebleSummary inmuebleSummary;

    private InmuebleSummaryDataProvider inmuebleSummaryDataProvider;

    private SubscriptionQueryResult<List<InmuebleSummary>, InmuebleSummary> fetchQueryResult;
    private InmuebleFilter filter = new InmuebleFilter(new UUID(0, 0));


    public InmueblesView(@Autowired QueryGateway queryGateway, @Autowired InmuebleService inmuebleService) {

        DataProvider<InmuebleSummary, Void> dataProvider =
            DataProvider.fromCallbacks(
                // First callback fetches items based on a query
                query -> {
                    // The index of the first item to load
                    int offset = query.getOffset();

                    // The number of items to load
                    int limit = query.getLimit();

                    List<InmuebleSummary> inmuebleSummaries = inmuebleService.fetchInmuebleSummary(offset, limit);

                    return inmuebleSummaries.stream();
                },
                // Second callback fetches the total number of items currently in the Grid.
                // The grid can then use it to properly adjust the scrollbars.
                query -> inmuebleService.getInmuebleSummaryCount());



        inmuebleSummaryDataProvider = new InmuebleSummaryDataProvider(queryGateway);

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
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("price").setAutoWidth(true);
        grid.setDataProvider(dataProvider);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            // if (event.getValue() != null) {
            //     Optional<InmuebleSummary> inmuebleSummaryFromBackend = inmuebleSummaryService
            //             .get(event.getValue().getId());
            //     // when a row is selected but the data is no longer available, refresh grid
            //     if (inmuebleSummaryFromBackend.isPresent()) {
            //         populateForm(inmuebleSummaryFromBackend.get());
            //     } else {
            //         refreshGrid();
            //     }
            // } else {
            //     clearForm();
            // }
        });

        // Configure Form
        // binder = new BeanValidationBinder<>(InmuebleSummary.class);

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

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setId("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setId("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        price = new TextField("price");
        Component[] fields = new Component[]{price};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
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
}
