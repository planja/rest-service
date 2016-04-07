package service.rest;

/**
 * Created by Никита on 01.04.2016.
 */

import actor.processingrequestparam.PreProcessingRequestParamActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import viewmodel.ParamsViewModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/rest_service")
public class RESTService {


    @GET
    @Path("/get/{some_param}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllCustomers(@PathParam("some_param") long id) {
        return Arrays.asList("hello get", "hi get");
    }

    @GET
    @Path("/block")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> block() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        return Arrays.asList("hello get", "hi get");
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    //@Consumes(MediaType.APPLICATION_JSON)
    public List<String> addCustomer(/*ParamsViewModel paramsViewModel*/) {
        PreProcessingRequestParamActor.paramsViewModel = new ParamsViewModel();
        ActorSystem system = ActorSystem.create("ActorSystem");
        ActorRef actorRef = system.actorOf(Props.create(PreProcessingRequestParamActor.class), "PreProcessingRequestParamActor");
        return Arrays.asList("hello get", "hi get");
    }

}
