package service.actor.request2parser;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import domain.ParserResult;
import parser.ParserType;
import parser.PreparedData;
import parser.interf.ParserActor;
import service.RequestData;
import service.actor.processingresult.ProcessingResultOfParserActor;
import service.adaptor.impl.AdaptorFactory;
import service.adaptor.interf.Adaptor;

public class RequestToParserActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            ActorRef processingResultOfParserActor = getContext().system().actorOf(Props.create(ProcessingResultOfParserActor.class), "processingResultOfParserActor");

            RequestData requestData = (RequestData) message;

            PreparedData preparedData = prepareData(requestData);

            ActorRef parserActor = getContext().system().actorOf(Props.create(getType(requestData).getParserClass()));
            parserActor.tell(message, self());

            processingResultOfParserActor.tell(new ParserResult(), getSelf());
        } else
            unhandled(message);
    }

    private PreparedData prepareData(RequestData requestData) {
        Adaptor adaptor = AdaptorFactory.getAdaptor(requestData.getParser());
        return adaptor.adaptData(requestData);
    }

    private ParserType getType(RequestData requestData) {
        return ParserType.valueOf(requestData.getParser().toUpperCase());
    }
}
