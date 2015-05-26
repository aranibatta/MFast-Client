/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mfast.evaluations;

import com.mfast.common.DBConnection;
import com.mfast.common.ServerResponse;
import com.mfast.login.LoginManager;
import com.mfast.evaluations.TestCountResponse;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;


/**
 * REST Web Service
 *
 * @author Saqib
 */
@Path("EvaluationRequest")
public class EvaluationRequestResource {

    int sessionId;
    String responseStr;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of EvaluationRequestResource
     */
    public EvaluationRequestResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mfast.evaluations.EvaluationRequestResource
     *
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
        ClientEvaluationRequest request = gson.fromJson(content, ClientEvaluationRequest.class);
        ServerResponse errorResponse = new ServerResponse();
        
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), errorResponse)){
            return gson.toJson(errorResponse);
        }
        
        //set up a session for the data collection application. 
        // return the session id back to the UI so that it can inquire about the status
        // of data
        switch(request.getRequestType()) {
            
            case ClientEvaluationRequest.REQUEST_TYPE_RECORD :
                if(this.addSession(request)){
                    
                    // get additional parameters of the patient that we want to pass to the DCApp
                    updatePatientInfo(request, request.getPatientId() );
                   
                    // call the data collection app with these parameters
                    DCAppSession dcappSession = new DCAppSession(request, sessionId);
                    Thread th = new Thread(dcappSession);
                    th.start();
                }
                break;
            case ClientEvaluationRequest.REQUEST_TYPE_TEST :
                if(this.addSession(request)){
                    // call the data collection app with these parameters
                    DCAppSession dcappSession = new DCAppSession(request, sessionId);
                    Thread th = new Thread(dcappSession);
                    th.start();
                }
                break;
            case ClientEvaluationRequest.REQUEST_TYPE_GET_COUNT :
                responseStr = getTestCount(request);
                break;                
        }
                
        return responseStr;
    }

    public String getTestCount(ClientEvaluationRequest request) {
        int patientId;
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        ServerResponse errorResponse = new ServerResponse();
        TestCountResponse response = new TestCountResponse();
        try{
           conn = DBConnection.getDBConnection();
           if(conn == null){
               throw new Exception();// this will get caught in the catch clause 
                                     // and result in the error server response
           }
           
           String sql = "SELECT COUNT(`timestamp`) AS testCountToday FROM `measurementsession` \n" +
                        "WHERE `sessionPatientId`= ? \n" +
                        "AND `evaluationId` = ? \n" +
                        "AND `weight` = ?\n" +
                        "AND `level` = ?\n" +
                        "AND `position` = ?\n" +
                        "AND `isCompleted` = 1\n" +
                        "AND `timestamp` >= ?;";
           
            // create an insert sql statement
           st = conn.prepareStatement(sql);
            
           st.setInt(1, request.getPatientId());
           st.setInt(2, request.getTestId());
           st.setFloat(3, request.getWeight());
           st.setInt(4, request.getLevel());
           st.setInt(5, request.getPosition());
           
           // get current date
           Calendar cal = Calendar.getInstance();
           cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
           cal.set(Calendar.MILLISECOND, 0);
         
           Date date = cal.getTime();
           Timestamp current = new Timestamp(date.getTime());
           st.setTimestamp(6, current);

           rs = st.executeQuery();
           
           if(!rs.next()){
                errorResponse.setMsg("getTestCount(): No records found");
                errorResponse.setRequestStatus(false);
                return gson.toJson(errorResponse);
            }
           
           response.setTestCountToday(rs.getInt("testCountToday"));
           
           rs.close();
           st.close();
           
          
           // make another query for the test name
           sql = "SELECT `evaluationDescription` FROM `evaluations` WHERE `evaluationID` = ?;";
           // create an insert sql statement
           st = conn.prepareStatement(sql);
           st.setInt(1, request.getTestId());
           
           rs = st.executeQuery();
           if(!rs.next()){
                errorResponse.setMsg("getTestCount(): No records found");
                errorResponse.setRequestStatus(false);
                return gson.toJson(errorResponse);
            }
           
           response.setTestName(rs.getString("evaluationDescription"));
           
           response.setRequestStatus(true);
           rs.close();
           
           }catch(Exception e){
            e.printStackTrace();
            errorResponse.setMsg("Internal Server Error: " + e.getMessage());
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
           // prepare a response
       // response.setMsg("Test history obtained successfully");
       // response.setRequestStatus(true);
        return gson.toJson(response);
    }
    
    private boolean addSession(ClientEvaluationRequest request) {
        Connection conn = null;
        PreparedStatement st = null;
        Gson gson = new Gson();
        ServerResponse response = new ServerResponse();
        try {
            conn = DBConnection.getDBConnection();
            if (conn == null) {
                throw new Exception();// this will get caught in the catch clause 
                // and result in the error server response
            }
            String sql = "INSERT INTO MEASUREMENTSESSION(evaluationId,sessionLocationId,sessionPatientId,sessionTherapistId,timeStamp,"
                    + "isCompleted,weight,level,position,clientVersion,optionalParams) "
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?);";
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, request.testId);
            st.setInt(2, request.getLocationId());
            st.setInt(3, request.getPatientId());
            st.setInt(4, request.getTherapistId());

            // set the timestamp
            java.util.Date date = new java.util.Date();

            Timestamp ts = new Timestamp(date.getTime());
            st.setTimestamp(5, ts);
            st.setBoolean(6, false);
            st.setFloat(7, request.getWeight());
            st.setInt(8, request.getLevel());
            st.setInt(9, request.getPosition());
            st.setFloat(10, request.getClientVersion());
            st.setString(11, request.getOptionalParameters());
            st.executeUpdate();
            // get the auto generated session id to return to the client
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            sessionId = rs.getInt(1);
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            responseStr = gson.toJson(response);
            return false;
        } finally {
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
        // prepare a response
        response.setMsg("Running requested evaluation");
        response.setSessionId(sessionId);
        response.setRequestStatus(true);
        responseStr = gson.toJson(response);
        return true;
    }
    
    
    private String updatePatientInfo(ClientEvaluationRequest request, int id)
    {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        ServerResponse errorResponse = new ServerResponse();
        // try to connec to server to grab this info
        try {
           conn = DBConnection.getDBConnection();
           if(conn == null){
               throw new Exception();// this will get caught in the catch clause 
                                     // and result in the error server response
           }
           
           String sql = "SELECT `height`, `gender` FROM `patient` \n" +
                        "WHERE `patientId`= ?;";
           
            // create an insert sql statement
           st = conn.prepareStatement(sql);
           // set id 
           st.setInt(1, id);
           // execute query
           rs = st.executeQuery();
           // get results
           if(!rs.next()){
                errorResponse.setMsg("getTestCount(): No records found");
                errorResponse.setRequestStatus(false);
                return gson.toJson(errorResponse);
            }

           request.setPatientHeight(rs.getFloat("height"));
           request.setPatientGender(rs.getString("gender"));
          
            // close connections
           rs.close();
           st.close();
           
           }catch(Exception e){
            e.printStackTrace();
            errorResponse.setMsg("Internal Server Error: " + e.getMessage());
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
        
        String output = "ok";
        return output;    // what kind of repsonse should we return???
    }
    
    
}
