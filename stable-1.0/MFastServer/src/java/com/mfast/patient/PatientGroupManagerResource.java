/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.patient;

import com.mfast.common.DBConnection;
import com.mfast.common.ServerResponse;
import com.mfast.login.LoginManager;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * REST Web Service
 *
 * @author Saqib
 */
@Path("patientGroupManager")
public class PatientGroupManagerResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PatientGroupManagerResource
     */
    public PatientGroupManagerResource() {
    }

    /**
     * Retrieves representation of an instance of com.mfast.patient.PatientGroupManagerResource
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
    public String postText(String content){
        String response = null;
        Gson gson = new Gson();
        ServerResponse errorResponse = new ServerResponse();
        PatientGroupRequest request = gson.fromJson(content, PatientGroupRequest.class);
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), errorResponse)){
            return gson.toJson(errorResponse);
        }
        switch(request.getRequestType()){
            case PatientGroupRequest.ADD_GROUP:
                response = addPatientGroup(request);
                break;
            case PatientGroupRequest.GET_GROUP_INFO:
                response = getPatientGroupInfo(request);
                break;
            case PatientGroupRequest.GET_LIST:
                response = getPatientGroupList(request);
                break;
        }
        return response;
    }
    
    public String addPatientGroup(PatientGroupRequest request){
        int groupId;
        Connection conn = null;
        PreparedStatement st = null;
        Gson gson =  new Gson();
        ServerResponse response = new ServerResponse();
        try{
           conn = DBConnection.getDBConnection();
           if(conn == null){
               throw new Exception();// this will get caught in the catch clause 
                                     // and result in the error server response
           }
            String sql = "INSERT INTO PATIENTGROUP(groupName,groupDescription) "
                    + "VALUES(?,?);";
            // create an insert sql statement
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            PatientGroup p = request.getGroup();
            st.setString(1, p.getGroupName());
            st.setString(2, p.getGroupDescription());
            st.executeUpdate();
            
            // get the auto generated session id to return to the client
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            groupId = rs.getInt(1);
            rs.close();
        }catch(Exception e){
            response.setMsg("Internal Server Error: " + e.toString());
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
           // prepare a response
        response.setPatientGroupId(groupId);
        response.setMsg("Patient group added successfully");
        response.setRequestStatus(true);
        return gson.toJson(response);
    }
    
    
    public String getPatientGroupInfo(PatientGroupRequest request){
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        // only one record is expected, so no need to loop
        PatientGroup p = new PatientGroup();
        ServerResponse response = new ServerResponse();
        try{
            st = conn.createStatement();
            String sql = null;
            sql = "SELECT * FROM patientGroup WHERE patientGroupId=" + request.getGroup().getPatientGroupId() + ";";
            rs = st.executeQuery(sql);
            if(!rs.next()){
                response.setMsg("Patient Group not found");
                response.setRequestStatus(false);
                return gson.toJson(response);
            }
            p.setGroupDescription(rs.getString("groupDescription"));
            p.setGroupName(rs.getString("groupName"));
            p.setPatientGroupId(rs.getInt("patientGroupId"));
            rs.close();
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
        return gson.toJson(p);
    }
    
    public String getPatientGroupList(PatientGroupRequest request){
        Vector<PatientGroup> list = new Vector<PatientGroup>();
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        ServerResponse response = new ServerResponse();
        PatientGroupList pl = new PatientGroupList();
        try{
            
            st = conn.createStatement();
            String sql = null;
            sql = "SELECT * FROM PatientGroup";
            rs = st.executeQuery(sql);
            if(!rs.next()){
                response.setMsg("No records found");
                response.setRequestStatus(false);
                return gson.toJson(response);
            }
            do{
                PatientGroup p = new PatientGroup();
                // we only need to return back combined first name and last name and patient id
                p.setGroupDescription(rs.getString("groupDescription"));
                p.setGroupName(rs.getString("groupName"));
                p.setPatientGroupId(rs.getInt("patientGroupId"));
                list.add(p);
            }while(rs.next());
            rs.close();
            PatientGroup[] pList = new PatientGroup[list.size()];
            for(int i = 0; i < list.size(); i++){
                pList[i] = list.elementAt(i);
            }
            pl.setGroupList(pList);
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
        return gson.toJson(pl);
    }    
}
