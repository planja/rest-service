package actor.processingresult;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;
import domain.ParserResult;

/**
 * Created by Никита on 05.04.2016.
 */
public class ProcessingResultOfParserActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ParserResult) {
            System.out.println("Processing result of parser");
            new Messenger().sendMessage();
            getContext().stop(getSelf());

        } else
            unhandled(msg);
    }
}
