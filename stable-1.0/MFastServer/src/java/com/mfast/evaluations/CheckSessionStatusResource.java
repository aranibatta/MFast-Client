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
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Saqib
 */
@Path("CheckSessionStatus")
public class CheckSessionStatusResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CheckSessionStatusResource
     */
    public CheckSessionStatusResource() {
    }

    /**
     * Retrieves representation of an instance of com.mfast.evaluations.CheckSessionStatusResource
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
        CheckSessionStatusRequest request = gson.fromJson(content, CheckSessionStatusRequest.class);
        PastResultsResponse response = new PastResultsResponse();
        return getPastResultsData(request);
    }
    
    String getPastResultsData(CheckSessionStatusRequest request){
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs;
        Gson gson =  new Gson();
        ServerResponse errorResponse = new ServerResponse();
        CheckSessionStatusResponse response = new CheckSessionStatusResponse();
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), errorResponse)){
            return gson.toJson(errorResponse);
        }
        try{
            st = conn.createStatement();
            String sql = "SELECT `isCompleted` FROM `measurementsession` WHERE `measurementSessionId`=" + request.getSessionId();
            rs = st.executeQuery(sql);
            if(!rs.next()){
                errorResponse.setMsg("No session found with session Id " + request.getSessionId());
                errorResponse.setRequestStatus(false);
                return gson.toJson(errorResponse);
            }
            response.setSessionId(request.getSessionId());
            response.setStatus(rs.getBoolean("isCompleted"));
            rs.close();
        }catch(Exception e){
            errorResponse.setMsg("Internal Server Error");
            errorResponse.setRequestStatus(false);
            return gson.toJson(errorResponse);
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
        return gson.toJson(response);
    }    
}
