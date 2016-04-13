package domain.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.List;

public class Repository extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
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
