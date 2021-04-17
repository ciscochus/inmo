package com.uoc.inmo.gui.data.provider;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.uoc.inmo.query.CountChangedUpdate;
import com.uoc.inmo.query.CountInmuebleSummariesQuery;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.CountInmuebleSummariesResponse;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.DataChangeEvent;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InmuebleSummaryDataProvider extends AbstractBackEndDataProvider<InmuebleSummary,Void>{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final QueryGateway queryGateway;
    
    public InmuebleSummaryDataProvider(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    private SubscriptionQueryResult<List<InmuebleSummary>, InmuebleSummary> fetchQueryResult;
    private SubscriptionQueryResult<CountInmuebleSummariesResponse, CountChangedUpdate> countQueryResult;
    
    private InmuebleFilter filter = new InmuebleFilter(new UUID(0, 0));

    @Override
    protected Stream<InmuebleSummary> fetchFromBackEnd(Query<InmuebleSummary, Void> query) {

        if (fetchQueryResult != null) {
            fetchQueryResult.cancel();
            fetchQueryResult = null;
        }

        FetchInmuebleSummariesQuery fetchInmuebleSummariesQuery = new FetchInmuebleSummariesQuery(query.getOffset(), query.getLimit(), filter);

        fetchQueryResult = queryGateway.subscriptionQuery(fetchInmuebleSummariesQuery,
                                                          ResponseTypes.multipleInstancesOf(InmuebleSummary.class),
                                                          ResponseTypes.instanceOf(InmuebleSummary.class));


        /*
        * Returning the initial result.
        */
        return fetchQueryResult.initialResult().block().stream();
        
        // List<InmuebleSummary> lista = new ArrayList<>();
        // InmuebleSummary inmueble = new InmuebleSummary();
        // inmueble.setId(new UUID(0, 0));
        // lista.add(inmueble);
        // return lista.stream();
    }

    @Override
    protected int sizeInBackEnd(Query<InmuebleSummary, Void> query) {
        if (countQueryResult != null) {
            countQueryResult.cancel();
            countQueryResult = null;
        }

        CountInmuebleSummariesQuery countInmuebleSummariesQuery = new CountInmuebleSummariesQuery(filter);
        logger.trace("submitting {}", countInmuebleSummariesQuery);
        countQueryResult = queryGateway.subscriptionQuery(countInmuebleSummariesQuery,
                                                          ResponseTypes.instanceOf(CountInmuebleSummariesResponse.class),
                                                          ResponseTypes.instanceOf(CountChangedUpdate.class));
        
        return countQueryResult.initialResult().block().getCount();
    }
    
}
