package com.guru.service.actor.messanger;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.HelloMessage;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class Messenger {
    public static Messenger instance;
    public static int count = 0;
    private static ActorRef remoteActor, myActor;

    private Messenger() {
        init();
    }

    private static void init() {
        final Config config = ConfigFactory.load().getConfig("applicationActor");
        ActorSystem system = ActorSystem.create("RemoteSystem", config);

        myActor = system.actorOf(Props.create(SenderMessageActor.class), "SenderMessageActor");

        final String path = "akka.tcp://RemoteApp@127.0.0.1:1719/user/RepositoryActor";
        remoteActor = system.actorFor(path);

    }

    public static void create() {
        if (instance == null)
            instance = new Messenger();
    }

    public static void sendMessage() {
        System.out.println("Send remote message");
        myActor.tell(new HelloMessage(remoteActor), null);
        System.out.println(count++);
    }

}