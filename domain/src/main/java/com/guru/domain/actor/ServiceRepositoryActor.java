package com.guru.domain.actor;

import akka.actor.UntypedActor;
import com.guru.domain.repository.TripRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service("serviceRepositoryActor")
@Scope("prototype")
public class ServiceRepositoryActor extends UntypedActor {

    @Inject
    private TripRepository tripRepository;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof List<?>) {
            tripRepository.findAll();
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
