package com.mfast.login;

import com.mfast.common.ServerResponse;
import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Saqib
 */
@Path("TherapistLogout")
public class TherapistLogoutResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TherapistLogoutResource
     */
    public TherapistLogoutResource() {
    }

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String postText(String content){
        LogoutRequest request = null;
        ServerResponse response = new ServerResponse();
        Gson gson = new Gson();
        try{
            request = gson.fromJson(content, LogoutRequest.class);
            return LoginManager.logoutTherapist(request);
        }catch(Exception e){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return gson.toJson(response);
        }
    }
}
