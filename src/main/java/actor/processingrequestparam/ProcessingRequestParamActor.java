package actor.processingrequestparam;

import akka.actor.UntypedActor;
import domain.Params;
import viewmodel.TestParamViewModel;

/**
 * Created by Никита on 05.04.2016.
 */
public class ProcessingRequestParamActor extends UntypedActor {


    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof TestParamViewModel) {
           // Params params = (Params) msg;//Обработка параметров
            System.out.println("Processing request param");
            getSender().tell(new Params(), getSelf());
        } else {
            unhandled(msg);
        }
    }
}
