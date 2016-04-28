package com.guru.rest;

import com.guru.domain.model.Trip;
import com.guru.parser.impl.qfparser.QFParser;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.beans.factory.annotation.Autowired;
import parser.exceptions.IncorrectCredentials;
import parser.utils.ComplexAward;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MainSpring {



    public static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public static final String BASE_URI = bundle.getString("url");
    public static final int PORT = Integer.parseInt(bundle.getString("port"));

    public static void main(String[] args) throws IOException, ParseException, InterruptedException, ExecutionException, IncorrectCredentials {
        SimpleDateFormat sdf_qr = new SimpleDateFormat("MM/dd/yyyy");
       QFParser qfParser = new QFParser();
        List dates = parser.test.Main.getDaysBetweenDates(sdf_qr.parse("12/10/2015"), sdf_qr.parse("12/15/2015"));
        List<Trip> trips = qfParser.getQantas(sdf_qr.parse("04/30/2016"), sdf_qr.parse("04/30/2016"), "LGW", "ADL", 1);

     /* parser.qf.QFParser qfParser1 = new parser.qf.QFParser();
        DefaultHttpClient client = parser.qf.QFParser.login("1905029755", "Myasnyankin", "7759");
      ComplexAward complexAward =   qfParser1.getQantas(client, sdf_qr.parse("04/30/2016"), sdf_qr.parse("04/30/2016"), "LGW", "ADL", 1);
*/
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
