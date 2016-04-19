package com.guru.service.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.guru.domain.actor.RepositoryActor;
import com.guru.service.RequestData;
import com.guru.service.adaptor.impl.AdaptorFactory;
import com.guru.service.adaptor.interf.Adaptor;
import com.guru.service.parser.ParserType;
import com.guru.vo.view.IMTAward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class RequestActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef repositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;
            ActorRef parserActor;
            for (String parserName : requestData.getParsers()) {
                System.out.println("parserName = " + parserName);
                parserActor = context().system().actorOf(Props.create(getParserClass(parserName)));
                parserActor.tell(message, self());
            }

/*            Timeout timeout = new Timeout(Duration.create(5, "seconds"));
            Future<Object> future = Patterns.ask(parserActor, message, timeout);*/
            //List<?> result = (List<?>) Await.result(future, timeout.duration());
            List<?> result = new ArrayList<>();

            Adaptor adaptor = AdaptorFactory.getAdaptor(requestData.getParsers().get(0));
            //List<IMTAward> awards = adaptor.adaptData(result);
            List<IMTAward> awards = new ArrayList<>();
            repositoryActor.tell(awards, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        repositoryActor = context().system().actorOf(Props.create(RepositoryActor.class));
    }

    private Class getParserClass(String parserName) {
        ParserType parserType = ParserType.valueOf(parserName.toUpperCase());
        return parserType.getParserClass();
    }
}
