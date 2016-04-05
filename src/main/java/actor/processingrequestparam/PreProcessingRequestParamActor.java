package actor.processingrequestparam;

import actor.request2parser.RequestToParserActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import domain.Params;
import viewmodel.TestParamViewModel;

/**
 * Created by Никита on 05.04.2016.
 */
public class PreProcessingRequestParamActor extends UntypedActor {

    public static TestParamViewModel paramViewModel;

    @Override
    public void preStart() {
        final ActorRef processingRequest = getContext().actorOf(Props.create(ProcessingRequestParamActor.class), "processingRequest");
        processingRequest.tell(paramViewModel, getSelf());//Вызываю актор обработки параметров
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof Params) {
            Params params = (Params) msg;
            ActorRef request2ParserActor = getContext().system().actorOf(Props.create(RequestToParserActor.class), "request2ParserActor");

            //Закончена обработка параметров, вызываю актор запроса к парсеру
            System.out.println("Processing request param stop");
            request2ParserActor.tell(params,getSelf());
            //getContext().stop(getSelf());
        } else
            unhandled(msg);
    }
}
