package domain.actor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import domain.HelloMessage;

import java.util.List;

public class Repository extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public static void main(String[] args) {
        //testing remote repository actor
        startSystem();
    }

    private static void startSystem() {
        final Config config = ConfigFactory.load().getConfig("repositoryActor");
        ActorSystem system = ActorSystem.create("RepositorySystem", config);
        system.actorOf(Props.create(Repository.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        log.info("i'm in");
        if (message instanceof String) {
            log.info("repository got it");
        }
        unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
    }
}
