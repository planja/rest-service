package actor.processingresult;

import actor.messanger.Messenger;
import akka.actor.UntypedActor;
import domain.ParserResult;

/**
 * Created by Никита on 05.04.2016.
 */
public class ProcessingResultOfParserActor extends UntypedActor {
    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof ParserResult) {
            System.out.println("Processing result of parser");
            Messenger.create().sendMessage();
            getContext().stop(getSelf());

        } else
            unhandled(msg);
    }
}
