package com.uoc.inmo.gui.provider;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import com.uoc.inmo.query.CountChangedUpdate;
import com.uoc.inmo.query.CountInmuebleSummariesQuery;
import com.uoc.inmo.query.FetchInmuebleSummariesQuery;
import com.uoc.inmo.query.entity.inmueble.CountInmuebleSummariesResponse;
import com.uoc.inmo.query.entity.inmueble.InmuebleSummary;
import com.uoc.inmo.query.filter.inmueble.InmuebleFilter;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.Query;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Synchronized;

public class InmuebleSummaryDataProvider extends AbstractBackEndDataProvider<InmuebleSummary, Void> {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private final QueryGateway queryGateway;

    /**
     * We need to keep track of our current subscriptions. To avoid subscriptions being modified while we are processing
     * query updates, the methods on these class are synchronized.
     */
    private SubscriptionQueryResult<List<InmuebleSummary>, InmuebleSummary> fetchQueryResult;
    private SubscriptionQueryResult<CountInmuebleSummariesResponse, CountChangedUpdate> countQueryResult;

    public InmuebleSummaryDataProvider(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @Getter
    @Setter
    @NonNull
    @SuppressWarnings("FieldMayBeFinal")
    private InmuebleFilter filter = new InmuebleFilter(new UUID(0, 0));

    @Override
    @Synchronized
    protected Stream<InmuebleSummary> fetchFromBackEnd(Query<InmuebleSummary, Void> query) {
        /*
         * If we are already doing a query (and are subscribed to it), cancel are subscription
         * and forget about the query.
         */
        if (fetchQueryResult != null) {
            fetchQueryResult.cancel();
            fetchQueryResult = null;
        }
        FetchInmuebleSummariesQuery fetchInmuebleSummariesQuery = new FetchInmuebleSummariesQuery(query.getOffset(), query.getLimit(), filter);
        logger.trace("submitting {}", fetchInmuebleSummariesQuery);
        /*
         * Submitting our query as a subscription query, specifying both the initially expected
         * response type (multiple CardSummaries) as wel as the expected type of the updates
         * (single CardSummary object). The result is a SubscriptionQueryResult which contains
         * a project reactor Mono for the initial response, and a Flux for the updates.
         */
        fetchQueryResult = queryGateway.subscriptionQuery(fetchInmuebleSummariesQuery,
                                                          ResponseTypes.multipleInstancesOf(InmuebleSummary.class),
                                                          ResponseTypes.instanceOf(InmuebleSummary.class));
        /*
         * Subscribing to the updates before we get the initial results.
         */
        fetchQueryResult.updates().subscribe(
                inmuebleSummary -> {
                    logger.trace("processing query update for {}: {}", fetchInmuebleSummariesQuery, inmuebleSummary);
                    /* This is a Vaadin-specific call to update the UI as a result of data changes. */
                    fireEvent(new DataChangeEvent.DataRefreshEvent<>(this, inmuebleSummary));
                });
        /*
         * Returning the initial result.
         */
        return fetchQueryResult.initialResult().block().stream();
    }

    @Override
    @Synchronized
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
        /* When the count changes (new gift cards issued), the UI has to do an entirely new query (this is
         * how the Vaadin grid works). When we're bulk issuing, it doesn't make sense to do that on every single
         * issue event. Therefore, we buffer the updates for 250 milliseconds using reactor, and do the new
         * query at most once per 250ms.
         */
        countQueryResult.updates().buffer(Duration.ofMillis(250)).subscribe(
                countChanged -> {
                    logger.trace("processing query update for {}: {}", countInmuebleSummariesQuery, countChanged);
                    /* This won't do, would lead to immediate new queries, looping a few times. */
                    executorService.execute(() -> fireEvent(new DataChangeEvent<>(this)));
                });
        return countQueryResult.initialResult().block().getCount();
    }
    

    @Synchronized
    void shutDown() {
        if (fetchQueryResult != null) {
            fetchQueryResult.cancel();
            fetchQueryResult = null;
        }
        if (countQueryResult != null) {
            countQueryResult.cancel();
            countQueryResult = null;
        }
    }
}
