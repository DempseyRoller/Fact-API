package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.Fact.Creator;
import org.bson.types.ObjectId;

import com.mongodb.MongoWriteException;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class FactRepository implements PanacheMongoRepository<Fact> {

    public Response insertFact(Fact fact) {

        try {
            // initializes save dates and additions
            fact.dbInit();
            persist(fact);
        } catch (MongoWriteException e) {
            return Response.status(Status.CONFLICT).entity(StatusConstants.ALREADY_IN_DATABASE).build();
        }
        return Response.status(Status.CREATED).entity(fact).build();
    }

    public Response loadFact(String id, Boolean asc, int limit) {
        // id
        if (id != null) {
            var objId = new ObjectId(id);
            var result = findById(objId);
            if (result == null) {
                return Response.status(Status.NOT_FOUND).entity(StatusConstants.NO_SUCH_ID).build();
            }
            return Response.ok(result).build();
        }
        // limit
        var page = Page.ofSize(limit);
        // sort
        var sort = asc ? Sort.ascending("firstSavedOn") : Sort.descending("firstSavedOn");
        // query
        var result = findAll(sort).page(page).list();

        return Response.ok(result).build();
    }

    public Response createFact(Fact newFact) {
        // create a new objectid, in case someone figured out that it's possible to pass
        // _id in the json
        newFact.setId(new ObjectId());
        newFact.setCreatedBy(Creator.USER);
        // insert to database
        return insertFact(newFact);
    }

    public Response updateFact(String id) {
        var objId = new ObjectId(id);
        var result = update("{'$currentDate':{'lastSavedOn':true},'$inc':{'additions':1}}").where("_id", objId);
        if (result == 0) {
            return Response.status(Status.NOT_FOUND).entity(StatusConstants.NO_SUCH_ID).build();
        }
        return Response.ok("{\"message\":\"Id " + id + " updated.\"}").build();
    }

    public Response deleteFact(String id) {
        var objId = new ObjectId(id);
        var result = deleteById(objId);
        if (!result) {
            return Response.status(Status.NOT_FOUND).entity(StatusConstants.NO_SUCH_ID).build();
        }
        return Response.ok("{\"message\":\"" + id + " deleted.\"}").build();
    }

    public Response deleteAllFacts() {
        var result = deleteAll();
        return Response.ok("{\"message\":\"All " + result + " entries deleted.\"}").build();
    }

}
