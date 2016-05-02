package com.guru.domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.domain.repository.TripRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/*@Service("serviceRepositoryActor")
@Scope("prototype")*/
public class ServiceRepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Inject
    private TripRepository tripRepository;


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List<?>) {
            List<Trip> trips = (List<Trip>) message;
            for (Trip trip : trips) {
                int size = trip.getFlights().size();
                log.info(trip.getFlightNumbers() + " flights in this trip: " + trip.getFlights().get(0));
            }
            tripRepository.save(trips);
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
