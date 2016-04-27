package com.guru.service.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.vo.transfer.RequestData;
import com.guru.service.parser.ParserType;
import org.springframework.stereotype.Component;

@Component
public class RequestActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);



    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;
            ActorRef parserActor = null;
            for (String parserName : requestData.getParsers()) {
                System.out.println("parserName = " + parserName);
                parserActor = context().system().actorOf(Props.create(getParserClass(parserName)));
                parserActor.tell(message, self());
            }

/*            Timeout timeout = new Timeout(Duration.create(5, "seconds"));
            Future<Object> future = Patterns.ask(parserActor, message, timeout);
            List<?> result = (List<?>) Await.result(future, timeout.duration());
            List<?> result = new ArrayList<>();

            Adaptor adaptor = AdaptorFactory.getAdaptor(requestData.getParsers().get(0));
            //List<IMTAward> awards = adaptor.adaptData(result);
            List<IMTAward> awards = new ArrayList<>();
            repositoryActor.tell(awards, self());*/
            //Messenger.sendMessage();
        } else if (message instanceof String) {
            log.info("got answer from repository");
        } else
            unhandled(message);
    }

    private Class getParserClass(String parserName) {
        ParserType parserType = ParserType.valueOf(parserName.toUpperCase());
        return parserType.getParserClass();
    }
}
