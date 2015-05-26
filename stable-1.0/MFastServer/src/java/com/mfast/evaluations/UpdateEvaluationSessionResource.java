/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfast.evaluations;

import com.mfast.common.DBConnection;
import com.mfast.common.ServerResponse;
import com.mfast.login.LoginManager;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;

/**
 *
 * @author gregorij
 */
@Path("UpdateEvaluationSession")
public class UpdateEvaluationSessionResource {
   @Context
    private UriInfo context;

    /**
     * Creates a new instance of UpdateEvaluationSessionResource
     */
    public UpdateEvaluationSessionResource() {
    }

    /**
     * Retrieves representation of an instance of com.mfast.evaluations.UpdateEvaluationSessionResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/plain")
    public String getText() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    public String postText(String content) {
        Gson gson = new Gson();
        UpdateEvaluationSessionRequest request = gson.fromJson(content, UpdateEvaluationSessionRequest.class);
        return updateSessionData(request);
    }
    
    String updateSessionData(UpdateEvaluationSessionRequest request){
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs;
        Gson gson =  new Gson();
        ServerResponse response = new ServerResponse();
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), response)){
            return gson.toJson(response);
        }
        
        try{
            st = conn.createStatement();
            String sql = "UPDATE measurementsession SET notes='" + request.getSessionNotes() + "' WHERE measurementSessionId=" + request.getSessionId();
            st.executeUpdate(sql);
        }catch(Exception e){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return gson.toJson(response);
        }finally{
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try            
        }
        response.setMsg("Session updated successfully");
        response.setRequestStatus(true);
        return gson.toJson(response);
    }     
}
