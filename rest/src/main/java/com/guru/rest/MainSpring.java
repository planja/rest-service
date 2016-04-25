package com.guru.rest;

import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainSpring {

    public static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final String BASE_URI = bundle.getString("url");
    public static final int PORT = Integer.parseInt(bundle.getString("port"));

    public static void main(String[] args) throws IOException {
        //RemoteSystem.create(ConfigFactory.load().getConfig("RemoteConfig"));
        //Messenger.create();
        startupServer();
    }

    private static void startupServer() throws IOException {
        HttpServer server = new HttpServer();
        NetworkListener listener = new NetworkListener("grizzly2", BASE_URI, PORT);
        server.addListener(listener);

        initSpringContext(server);

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
        server.start();
        System.out.println(String.format("Application started. Stop the application using CTRL+C"));

    }

    private static void initSpringContext(HttpServer server) {
        WebappContext context = new WebappContext("ctx", "/");
        final ServletRegistration registration = context.addServlet("spring", new SpringServlet());
        registration.addMapping("/*");
        registration.setInitParameter("javax.ws.rs.Application", "com.guru.rest.config.JerseyConfig");

        context.addContextInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        context.addContextInitParameter("contextConfigLocation", "com.guru.rest.config.RestConfig");
        context.addListener("org.springframework.web.context.ContextLoaderListener");
        context.addListener("org.springframework.web.context.request.RequestContextListener");
        context.deploy(server);
    }
}
