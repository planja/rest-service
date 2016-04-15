package domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.List;

public class RepositoryActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("i'm in");
        if (message instanceof List<?>) {
            log.info("create");
        }
        unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
    }

    @Override
    public void postStop() {
        context().system().shutdown();
    }
}
