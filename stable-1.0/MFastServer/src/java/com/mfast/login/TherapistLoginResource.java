package com.mfast.login;

import com.mfast.common.ServerResponse;
import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Saqib
 */
@Path("therapistlogin")
public class TherapistLoginResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TherapistLoginResource
     */
    public TherapistLoginResource() {
    }

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String postText(String content){
        LoginRequest request = null;
        ServerResponse response = new ServerResponse();
        Gson gson = new Gson();
        try{
            request = gson.fromJson(content, LoginRequest.class);
            return LoginManager.loginTherapist(request);
        }catch(Exception e){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return gson.toJson(response);
        }
    }
}
