package com.guru.service.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.service.parser.ParserType;
import com.guru.vo.transfer.RequestData;
import org.springframework.stereotype.Component;

import static com.guru.domain.config.SpringExtension.SpringExtProvider;

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
                parserActor = getContext().actorOf(SpringExtProvider.get(getContext().system()).props(getClassName(parserName)));
                // parserActor = context().system().actorOf(Props.create(getParserClass(parserName)));
                parserActor.tell(message, self());
            }
        } else
            unhandled(message);
    }

    private String getClassName(String parserName) {
        return "parser" + parserName;
    }

    private Class getParserClass(String parserName) {
        ParserType parserType = ParserType.valueOf(parserName.toUpperCase());
        return parserType.getParserClass();
    }
}
