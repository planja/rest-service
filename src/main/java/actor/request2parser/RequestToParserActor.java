package actor.request2parser;

import actor.processingresult.ProcessingResultOfParserActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import domain.Params;
import domain.ParserResult;

/**
 * Created by Никита on 05.04.2016.
 */
public class RequestToParserActor extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof Params) {
            Params params = (Params) msg;
            ActorRef processingResultOfParserActor = getContext().system().actorOf(Props.create(ProcessingResultOfParserActor.class), "processingResultOfParserActor");

            //Закончен запрос к парсеру, вызываю актор обработки результата
            System.out.println("Request2ParserStop");
            processingResultOfParserActor.tell(new ParserResult(), getSelf());
        } else
            unhandled(msg);
    }
}
