package rest;

/**
 * Created by Никита on 01.04.2016.
 */

import akka.actor.ActorRef;
import org.springframework.stereotype.Component;
import service.RequestData;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Component
@Path("/")
public class RESTService {

    @Inject
    public ActorRef requestActor;

    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("parser") String parser,
                        @PathParam("user") String user,
                        @PathParam("origin") String origin,
                        @PathParam("destination") String destination,
                        @PathParam("ow_start_date") Date ow_start_date,
                        @PathParam("ow_end_date") Date ow_end_date,
                        @PathParam("rt_start_date") Date rt_start_date,
                        @PathParam("rt_end_date") Date rt_end_date,
                        @PathParam("ow_except_dates") List<Date> ow_except_dates,
                        @PathParam("rt_except_dates") List<Date> rt_except_dates,
                        @PathParam("seats") int seats,
                        @PathParam("cabins") List<String> cabins,
                        @PathParam("type") String type,
                        @PathParam("request_id") int request_id,
                        @PathParam("user_id") int user_id) {

        RequestData viewModel = new RequestData(parser, user, origin, destination, ow_start_date, ow_end_date,
                rt_start_date, rt_end_date, ow_except_dates, rt_except_dates, seats, cabins, type, request_id, user_id);
        requestActor.tell(viewModel, requestActor);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(RequestData viewModel) {
        requestActor.tell(viewModel, requestActor);
        return Response.status(Response.Status.OK).build();
    }
}
