package domain; /**
 * Created by Никита on 04.04.2016.
 */

import akka.actor.ActorRef;

import java.io.Serializable;

public class HelloMessage implements Serializable {
    private ActorRef receiver;
    private String text;

    public HelloMessage() {
    }

    public HelloMessage(ActorRef receiver) {
        this.receiver = receiver;
    }

    public ActorRef getReceiver() {
        return receiver;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}