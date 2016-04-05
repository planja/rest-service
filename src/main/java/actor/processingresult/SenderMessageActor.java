package actor.processingresult; /**
 * Created by Никита on 04.04.2016.
 */
import akka.actor.UntypedActor;
import domain.HelloMessage;

public class SenderMessageActor extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof HelloMessage) {
            HelloMessage msg = (HelloMessage) message;
            if (msg.getReceiver() !=null){
                msg.setText("Hello");
                msg.getReceiver().tell(msg, getSelf());
            }
        } else {
            System.out.println("UnHandled Message Received" );
            unhandled(message);
        }
    }

}