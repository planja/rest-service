package com.guru.rest;

//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;

/**
 * com.guru.rest.Main class.
 */
public class MainOld {
    // Base URI the Grizzly HTTP server will listen on
/*    public static String BASE_URI = "http://localhost:";

    *//**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     *//*
    public static HttpServer startServer() {

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File("com.guru.rest/src/main/resources/config.properties")));
            Main.BASE_URI = Main.BASE_URI + props.getProperty("port") + "/";
        } catch (IOException io) {
            //System.out.println(io);
        }


        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.com.guru.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.guru.rest");

        // create and start a new instance of grizzly http server

        // exposing the Jersey application at BASE_URI
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);

        server.getServerConfiguration().addHttpHandler(
                new HttpHandler() {
                    public void com.guru.service(Request request, Response response) throws Exception {
                        final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                        final String date = format.format(new Date(System.currentTimeMillis()));
                        response.setContentLength(date.length());
                        response.getWriter().write(date);
                    }
                },
                "/time");
        return server;
    }

    *//**
     * com.guru.rest.Main method.
     *
     * @param args
     * @throws IOException
     *//*
    public static void main(String[] args) throws IOException {
        try {
            Messenger.create();
            final HttpServer server = startServer();
            System.out.println(String.format("Jersey app started with WADL available at "
                    + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
            System.in.read();
            server.stop();
        } catch (Exception e) {
            System.err.println(e);
        }
    }*/
}

