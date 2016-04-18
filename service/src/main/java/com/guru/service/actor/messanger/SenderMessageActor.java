package com.guru.service.actor.messanger;

import akka.actor.UntypedActor;


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