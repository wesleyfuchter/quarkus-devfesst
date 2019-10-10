package com.wesleyfuchter.peopleservice;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Optional;

@Path("/people")
@Slf4j
public class PeopleResource {

    private final PeopleService peopleService;

    @Inject
    public PeopleResource(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        return Response.ok(peopleService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") String id) {
        Optional<People> optionalPeople = peopleService.findById(id);
        return optionalPeople.isPresent() ?
                Response.ok(optionalPeople.get()).build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        Optional<People> optionalPeople = peopleService.findById(id);
        optionalPeople.ifPresent(people -> peopleService.delete(people.getId()));
        return optionalPeople.isPresent() ?
                Response.noContent().build() :
                Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(People people) throws Exception {
        People savedPeople = peopleService.save(people);
        return Response.created(new URI("/people/" + savedPeople.getId())).entity(savedPeople).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(@PathParam("id") String id, People people) {
        Optional<People> optionalPeople = peopleService.findById(id);
        if (!optionalPeople.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(peopleService.update(id, people)).build();
    }

}