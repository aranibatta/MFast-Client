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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
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
@Path("pastresults")
public class PastResultsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PastresultsResource
     */
    public PastResultsResource() {
    }

    /**
     * Retrieves representation of an instance of com.mfast.evaluations.PastResultsResource
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
        PastResultsRequest request = gson.fromJson(content, PastResultsRequest.class);
        return getPastResultsData(request);
    }
    
    public String getPastResultsData(PastResultsRequest request){
        Vector<PastResultData> list = new Vector<PastResultData>();
        Connection conn = DBConnection.getDBConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        ServerResponse errorResponse = new ServerResponse();
        PastResultsResponse response = new PastResultsResponse();
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), errorResponse)){
            return gson.toJson(errorResponse);
        }
        
        try{
            
            String sql = null;
            sql = "SELECT `timestamp`, `weight` FROM `measurementsession` WHERE `sessionPatientId`=? AND `timestamp` > ? AND `timestamp` < ?;";
            st = conn.prepareStatement(sql);
            st.setInt(1, request.getPatientId());
            Date dt = com.mfast.common.Util.getDateFromString(request.getStartDate());
            Timestamp start = new Timestamp(dt.getTime());
            dt = com.mfast.common.Util.getDateFromString(request.getEndDate());
            Timestamp end = new Timestamp(dt.getTime());
            st.setTimestamp(2, start);
            st.setTimestamp(3, end);
            rs = st.executeQuery();
            if(!rs.next()){
                errorResponse.setMsg("No records found");
                errorResponse.setRequestStatus(false);
                return gson.toJson(errorResponse);
            }
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            do{
                PastResultData p = new PastResultData();
                // we only need to return back combined first name and last name and patient id
                p.setDate(df.format(rs.getTimestamp("timestamp")));
                p.setWeight(rs.getFloat("weight"));
                list.add(p);
            }while(rs.next());
            rs.close();
            PastResultData[] pList = new PastResultData[list.size()];
            for(int i = 0; i < list.size(); i++){
                pList[i] = list.elementAt(i);
            }
            response.setData(pList);
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
