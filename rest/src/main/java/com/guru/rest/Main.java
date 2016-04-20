package com.guru.rest;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class Main {

    public static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final String BASE_URI = bundle.getString("url");
    public static final String PORT = bundle.getString("port");

    public static void main(String[] args) throws IOException {
        startupServer();
        //startupActorSystem();
    }

    private static void startupServer() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig().packages("com.guru");
        org.glassfish.grizzly.http.server.HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI + PORT + "/"), resourceConfig);
        server.getServerConfiguration().addHttpHandler(
                new HttpHandler() {
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

/*    private static void startupActorSystem() {
        final Config config = ConfigFactory.load().getConfig("appActor");
        ActorSystem.create("ApplicationSystem", config);
    }*/
}
