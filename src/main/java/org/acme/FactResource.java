package org.acme;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/facts")
public class FactResource {

    @RestClient
    FactWebService factWebService;

    @Inject
    FactRepository db;

    // Get a random fact from cat-facts api, save it to the database if parameter
    // save=true
    @GET
    @Path("/web")
    @Produces(MediaType.APPLICATION_JSON)
    public Response randomFact(@DefaultValue("false") @QueryParam("save") Boolean save) {
        var fact = factWebService.getRandomFact();
        if (save) {
            return db.insertFact(fact);
        }

        return Response.ok(fact).build();
    }

    // Get a fact by id from cat-facts api, save it to the database if parameter
    // save=true
    @GET
    @Path("/web/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response factById(String id, @DefaultValue("false") @QueryParam("save") Boolean save) {
        if (!ObjectId.isValid(id)) {
            return Response.status(Status.BAD_REQUEST).entity(StatusConstants.INVALID_OBJECTID).build();
        }
        Fact fact;
        try {
            fact = factWebService.getFactById(id);
        } catch (WebApplicationException e) {
            return e.getResponse();
        }
        if (save) {
            return db.insertFact(fact);
        }

        return Response.ok(fact).build();
    }

    // Load from the database
    @GET
    public Response loadFact(@QueryParam("id") String id, @DefaultValue("true") @QueryParam("asc") Boolean asc,
            @DefaultValue("5") @QueryParam("limit") int limit) {
        if (id != null && !ObjectId.isValid(id)) {
            return Response.status(Status.BAD_REQUEST).entity(StatusConstants.INVALID_OBJECTID).build();
        }
        return db.loadFact(id, asc, limit);
    }

    // Add an own fact to the database
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewFact(Fact newFact) {
        if (newFact == null) {
            return Response.status(Status.BAD_REQUEST).entity(StatusConstants.MISSING_JSON).build();
        }
        // Check that all the needed fields are filled
        if (newFact.getText() == null || newFact.getText().isBlank() || newFact.getType() == null
                || newFact.getType().isBlank()) {
            return Response.status(Status.BAD_REQUEST).entity(StatusConstants.MISSING_FIELDS).build();
        }

        return db.createFact(newFact);
    }

    // Add a "new" entry of an existing entry. (Sets a new lastSavedOn and adds one
    // point to "additions" field.)
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addToFact(String id) {
        if (!ObjectId.isValid(id)) {
            return Response.status(Status.BAD_REQUEST).entity(StatusConstants.INVALID_OBJECTID).build();
        }
        return db.updateFact(id);
    }

    // Delete all facts from the database
    @DELETE
    public Response deleteAll(@DefaultValue("false") @QueryParam("deleteAll") Boolean deleteAll) {
        if (!deleteAll) {
            return Response.status(Status.FORBIDDEN).entity(StatusConstants.CONFIRM_DELETEALL).build();
        }
        return db.deleteAllFacts();
    }

    // Delete fact from the database by id
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(String id) {
        if (!ObjectId.isValid(id)) {
            return Response.status(400).entity(StatusConstants.INVALID_OBJECTID).build();
        }
        return db.deleteFact(id);
    }

}
