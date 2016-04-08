package actor.processingresult;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import domain.HelloMessage;

public class Messenger {
    private ActorSystem system;
    private ActorRef remoteActor, myActor;

    public Messenger() {
        system = ActorSystem.create("RemoteSystem", ConfigFactory.load()
                .getConfig("ActorConfig"));
        myActor = system.actorOf(Props.create(SenderMessageActor.class), "SenderMessageActor");
        remoteActor = system
                .actorFor("akka.tcp://RemoteSystem@127.0.0.1:2552/user/Actor");
    }


    public void sendMessage() {
        myActor.tell(new HelloMessage(remoteActor), null);
        system.shutdown();
    }

}