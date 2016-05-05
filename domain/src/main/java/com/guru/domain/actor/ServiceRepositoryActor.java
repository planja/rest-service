package com.guru.domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.MileCostRepository;
import com.guru.domain.repository.QueryRepository;
import com.guru.domain.repository.TripRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

public class ServiceRepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private TripRepository tripRepository;

    @Inject
    private MileCostRepository mileCostRepository;

    @Override
    public void onReceive(Object message) throws Exception {
        try {
            if (message instanceof List<?>) {
                List<Trip> trips = (List<Trip>) message;
                tripRepository.save(trips);
            }
            else {
                unhandled(message);
            }
        } catch (Exception e) {
            log.error(e, "Cannot insert parse data to the database");
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
