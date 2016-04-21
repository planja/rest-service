package com.guru.domain.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by Никита on 21.04.2016.
 */
public class RemoteSystem {

    public static RemoteSystem instance;


    private RemoteSystem() {
        RemoteInit();
    }

    private static void RemoteInit() {
        ActorSystem system = ActorSystem.create("RemoteApp", ConfigFactory.load()
                .getConfig("RemoteConfig"));
        ActorRef actor = system.actorOf(Props.create(RepositoryActor.class), "RepositoryActor");

    }

    public static void create() {
        if (instance == null)
            instance = new RemoteSystem();
    }

}
