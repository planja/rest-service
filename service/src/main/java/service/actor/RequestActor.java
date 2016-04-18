package service.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import domain.actor.RepositoryActor;
import service.RequestData;
import service.adaptor.impl.AdaptorFactory;
import service.adaptor.interf.Adaptor;
import service.parser.ParserType;
import vo.view.IMTAward;

import java.util.ArrayList;
import java.util.List;

public class RequestActor extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef repositoryActor;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RequestData) {
            RequestData requestData = (RequestData) message;

            ParserType parserType = getType(requestData);

            ActorRef parserActor = context().system().actorOf(Props.create(parserType.getParserClass()));
            parserActor.tell(message, self());

/*            Timeout timeout = new Timeout(Duration.create(5, "seconds"));
            Future<Object> future = Patterns.ask(parserActor, message, timeout);*/
            //List<?> result = (List<?>) Await.result(future, timeout.duration());
            List<?> result = new ArrayList<>();

            Adaptor adaptor = AdaptorFactory.getAdaptor(parserType);
            //List<IMTAward> awards = adaptor.adaptData(result);
            List<IMTAward> awards = new ArrayList<>();
            repositoryActor.tell(awards, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        //final Config config = ConfigFactory.load().getConfig("repositoryActor");
        //ActorSystem system = ActorSystem.create("RemoteSystem", config);
        //final String path = "akka.tcp://RepositorySystem@127.0.0.1:2554/user/repositoryActor";
        repositoryActor = context().system().actorOf(Props.create(RepositoryActor.class));
    }

    private ParserType getType(RequestData requestData) {
        return ParserType.valueOf(requestData.getParser().toUpperCase());
    }
}
