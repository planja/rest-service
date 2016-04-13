package service.actor;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import domain.actor.Repository;
import parser.interf.ParserActor;
import scala.concurrent.Future;
import service.actor.messanger.SenderMessageActor;
import vo.transfer.ParserResult;
import parser.ParserType;
import parser.PreparedData;
import service.RequestData;
import service.actor.processingresult.ProcessingResultOfParserActor;
import service.adaptor.impl.AdaptorFactory;
import service.adaptor.interf.Adaptor;
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

            //Future<List<?>> result =
            Adaptor adaptor = AdaptorFactory.getAdaptor(parserType);
            List<IMTAward> awards = adaptor.adaptData(new ArrayList<>());

            repositoryActor.tell(awards, self());
        } else
            unhandled(message);
    }

    @Override
    public void preStart() throws Exception {
        //final Config config = ConfigFactory.load().getConfig("repositoryActor");
        //ActorSystem system = ActorSystem.create("RemoteSystem", config);
        //final String path = "akka.tcp://RepositorySystem@127.0.0.1:2554/user/repositoryActor";
        repositoryActor = context().system().actorOf(Props.create(Repository.class));
    }

    private ParserType getType(RequestData requestData) {
        return ParserType.valueOf(requestData.getParser().toUpperCase());
    }
}
