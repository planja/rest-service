package domain.actor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import domain.HelloMessage;
import vo.view.IMTAward;

import java.lang.reflect.Field;
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
