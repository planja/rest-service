package actor.messanger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import domain.HelloMessage;

public class Messenger {
    private static ActorRef remoteActor, myActor;
    public static Messenger instance;
    public static int count = 0;

    private Messenger() {
        ActorSystem system = ActorSystem.create("RemoteSystem", ConfigFactory.load()
                .getConfig("ActorConfig"));
        myActor = system.actorOf(Props.create(SenderMessageActor.class), "SenderMessageActor");
        remoteActor = system
                .actorFor("akka.tcp://RemoteSystem@127.0.0.1:2552/user/Actor");
    }

    public static Messenger create() {
        if (instance == null) {
            instance = new Messenger();
        } else {
            return instance;
        }
        return instance;
    }

    public void sendMessage() {
        System.out.println("Send remote message");
        myActor.tell(new HelloMessage(remoteActor), null);
        System.out.println(count++);
    }

}