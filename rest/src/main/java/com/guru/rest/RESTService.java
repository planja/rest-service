package com.guru.rest;

/**
 * Created by Никита on 01.04.2016.
 */

import akka.actor.ActorRef;
import com.guru.vo.transfer.RequestData;
import com.guru.vo.transfer.RequestDataViewModel;
import com.guru.vo.transfer.Status;
import com.guru.vo.transfer.StatusCount;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;

@Component
@Path("/")
public class RESTService {

    @Inject
    public ActorRef requestActor;

   /* @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("parsers") List<String> parsers,
                        @PathParam("user") String user,
                        @PathParam("origin") String origin,
                        @PathParam("destination") String destination,
                        @PathParam("ow_start_date") Date ow_start_date,
                        @PathParam("ow_end_date") Date ow_end_date,
                        @PathParam("rt_start_date") Date rt_start_date,
                        @PathParam("rt_end_date") Date rt_end_date,
                        @PathParam("ow_except_dates") List<ExceptDate> ow_except_dates,
                        @PathParam("rt_except_dates") List<ExceptDate> rt_except_dates,
                        @PathParam("seats") int seats,
                        @PathParam("cabins") List<String> cabins,
                        @PathParam("type") String type,
                        @PathParam("request_id") int request_id,
                        @PathParam("user_id") int user_id) {

       RequestData viewModel = new RequestData();
        requestActor.tell(viewModel, requestActor);
        return Response.status(Response.Status.OK).build();
    }*/

    @POST
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(RequestDataViewModel requestDataViewModel) throws ParseException {
        RequestData requestData = requestDataViewModel.toRequestData();

        Status.statusCountList.add(
                new StatusCount((long) requestData.getRequest_id(), 0, Status.CalculateStatus(requestData)));

        requestActor.tell(requestData, requestActor);
        return Response.status(Response.Status.OK).build();
    }
}
