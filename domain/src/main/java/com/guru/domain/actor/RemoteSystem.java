package com.guru.domain.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;

/**
 * Created by Никита on 21.04.2016.
 */
public class RemoteSystem {

    public static RemoteSystem instance;


    private RemoteSystem(Config config) {
        RemoteInit(config);
    }

    private static void RemoteInit(Config config) {
        ActorSystem system = ActorSystem.create("RemoteApp", config);
        ActorRef actor = system.actorOf(Props.create(RepositoryActor.class), "RepositoryActor");

    }

    public static void create(Config config) {
        if (instance == null)
            instance = new RemoteSystem(config);
    }

}
