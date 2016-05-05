package com.guru.domain.actor;


import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.MileCost;
import com.guru.domain.model.Query;
import com.guru.domain.model.Trip;
import com.guru.domain.model.TripCost;
import com.guru.domain.repository.MileCostRepository;
import com.guru.domain.repository.QueryRepository;
import com.guru.domain.repository.TripCostRepository;
import com.guru.domain.repository.TripRepository;
import com.guru.domain.service.cost.CalculateCost;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class ServiceRepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private TripRepository tripRepository;

    @Inject
    private MileCostRepository mileCostRepository;

    @Inject
    private TripCostRepository tripCostRepository;

    @Inject
    private QueryRepository queryRepository;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List<?>) {
            List<Trip> trips = (List<Trip>) message;
            for (Trip trip : trips) {
                int size = trip.getFlights().size();
                log.info(trip.getFlightNumbers() + " flights in this trip: " + trip.getFlights().get(0));
            }
            List<Trip> save = StreamSupport.stream(Spliterators.spliteratorUnknownSize(tripRepository.save(trips).iterator(), Spliterator.ORDERED), false)
                    .collect(Collectors.toCollection(ArrayList::new));
            if (trips.size() != 0) {
                //queryRepository.updateStatus(trips.get(0).getQueryId(),100);

                List<TripCost> tripCosts = CalculateCost.calc(save);
                tripCostRepository.save(tripCosts);
            }


        } else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() {
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
