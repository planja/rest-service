package com.guru.rest;

import com.guru.domain.config.DataConfig;
import com.guru.domain.model.Query;
import com.guru.domain.model.Trip;
import com.guru.domain.repository.QueryRepository;
import com.guru.domain.repository.TripRepository;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import parser.ua.UANParser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MainSpring {

    public static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final String BASE_URI = bundle.getString("url");
    public static final int PORT = Integer.parseInt(bundle.getString("port"));

    public static void main(String[] args) throws IOException,ParseException,InterruptedException {
        /* String date = "04/25/2016";
        String origin = "EZE";
        String destination = "ABZ";
        UANParser uaParser = new UANParser();
        List flights1 = uaParser.getUnited(date, origin, destination, 1, "E");
        //RemoteSystem.create(ConfigFactory.load().getConfig("RemoteConfig"));
        //Messenger.create();*/
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
        WebappContext context = new WebappContext("ctx","/");
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
