package com.guru.domain.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

@Component
public class RepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef serviceRepositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Long) {
            serviceRepositoryActor.tell(message, self());
        }
        if (message instanceof List<?>) {
            log.info("create");
            serviceRepositoryActor.tell(message, self());
        } else {
            unhandled(message);
        }
    }

    @Override
    public void preStart() throws Exception {
        serviceRepositoryActor = getContext().actorOf(SpringExtProvider.get(getContext().system()).props("serviceRepositoryActor"));
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
