package com.guru.domain;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.guru.domain.actor.RepositoryActor;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;

/**
 * Created by Никита on 20.04.2016.
 */

public class Main {
    public static void main(String[] args) throws IOException {ActorSystem system;
        system = ActorSystem.create("RemoteApp", ConfigFactory.load()
                .getConfig("RemoteConfig"));
        ActorRef actor = system.actorOf(Props.create(RepositoryActor.class), "RepositoryActor");
    }
}
