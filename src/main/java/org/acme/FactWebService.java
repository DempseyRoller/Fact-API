package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "cat-fact-api")
public interface FactWebService {

    @Path("random")
    @GET
    Fact getRandomFact();

    @GET
    @Path("{_id}")
    Fact getFactById(@PathParam("_id") String _id) throws WebApplicationException;

}
