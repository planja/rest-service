package com.guru.rest;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.guru.domain.actor.RemoteSystem;
import com.guru.service.actor.messanger.Messenger;
import com.typesafe.config.ConfigFactory;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Main {


    public static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final String BASE_URI = bundle.getString("url");
    public static final String PORT = bundle.getString("port");

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
       /* String date = "25/04/2016";
        String origin = "SYD";
        String destination = "FRA";
        UAParser uaParser = new UAParser();
        List flights1 = uaParser.getUnited(date, origin, destination, 1, "E");
        uaParser.getUnited("04/18/2016", origin, destination, 1, "E");
        uaParser.getUnited("04/19/2016", origin, destination, 1, "E");
        uaParser.getUnited("04/20/2016", origin, destination, 1, "E");
        uaParser.getUnited("04/21/2016", origin, destination, 1, "E");
        uaParser.getUnited("04/22/2016", origin, destination, 1, "E");
        uaParser.getUnited("04/23/2016", origin, destination, 1, "E");
        System.out.println(flights1);*/


        RemoteSystem.create(ConfigFactory.load().getConfig("repositoryConfig"));
        Messenger.create();
        startupServer();
    }

    private static void startupServer() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.guru");
        resourceConfig.register(JacksonJsonProvider.class);
        org.glassfish.grizzly.http.server.HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI + PORT + "/"), resourceConfig);
        server.getServerConfiguration().addHttpHandler(
                new HttpHandler() {
                    @Override
                    public void service(Request request, Response response) throws Exception {
                        final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                        final String date = format.format(new Date(System.currentTimeMillis()));
                        response.setContentLength(date.length());
                        response.getWriter().write(date);
                    }
                },
                "/time");
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%s application.wadl\nHit enter to stop it...", BASE_URI));
    }
}
