package com.cjcodes.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import com.cjcodes.TestConfiguration;
import com.cjcodes.actors.messages.IdCreate;
import com.cjcodes.actors.messages.IdDelete;
import com.cjcodes.actors.messages.IdGet;
import com.cjcodes.actors.messages.IdList;

import akka.actor.ActorRef;
import scala.concurrent.Future;

import static akka.pattern.PatternsCS.ask;

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {
    private final ActorRef datastore;
    private final Long timeout;

    public TestResource(ActorRef datastore, Long timeout) {
        this.datastore = datastore;
        this.timeout = timeout;
    }

    @GET
    public void list(@Suspended final AsyncResponse response) {
        ask(datastore, new IdList(), this.timeout)
            .thenAccept(result -> {
                response.resume(result);
            });
    }

    @Path("{id}")
    @POST
    public void add(@Suspended final AsyncResponse response, @PathParam("id") String id) {
        ask(datastore, new IdCreate(id), this.timeout)
            .thenAccept(result -> {
                response.resume(id);
            });
    }

    @Path("{id}")
    @GET
    public void get(@Suspended final AsyncResponse response, @PathParam("id") String id) {
        ask(datastore, new IdGet(id), this.timeout)
            .thenAccept(result -> {
                response.resume(result);
            })
            .exceptionally(throwable -> {
                response.resume(new WebApplicationException(404));
                return null;
            });
    }

    @Path("{id}")
    @DELETE
    public void remove(@Suspended final AsyncResponse response, @PathParam("id") String id) {
        ask(datastore, new IdDelete(id), this.timeout)
            .thenAccept(result -> {
                response.resume(result);
            })
            .exceptionally(throwable -> {
                response.resume(new WebApplicationException(404));
                return null;
            });
    }
}
