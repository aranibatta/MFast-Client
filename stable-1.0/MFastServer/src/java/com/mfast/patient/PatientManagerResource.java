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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
@Path("patientmanager")
public class PatientManagerResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PatientManagerResource
     */
    public PatientManagerResource() {
    }

    /**
     * Retrieves representation of an instance of com.mfast.patient.PatientManagerResource
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
        PatientRequest request = gson.fromJson(content, PatientRequest.class);
        if(!LoginManager.verifyLoginSession(request.getLoginSessionId(), errorResponse)){
            return gson.toJson(errorResponse);
        }
        switch(request.getRequestType()){
            case PatientRequest.ADD_PATIENT:
                response = addPatient(request);
                break;
            case PatientRequest.GET_PATIENT_INFO:
                response = getPatientInfo(request);
                break;
            case PatientRequest.GET_PATIENT_LIST:
                response = getPatientList(request);
                break;
            case PatientRequest.EDIT_PATIENT_INFO:
                response = editPatient(request);
        }
        return response;
    }
    
    public String addPatient(PatientRequest request){
        int patientId;
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
            String sql = "INSERT INTO PATIENT(firstName,lastName,DOB,gender,"
                    + "leftArmLength,rightArmLength,handDominance,therapistId,"
                    + "locationId,"
                    + "patientGroupId,customPatientId,rightStern2acJointLength,"
                    + "leftStern2acJointLength,rightUpperArmLength,leftUpperArmLength,"
                    + "height,weight,rightForearmLength,leftforearmLength,notes,"
                    + "patientSecondGroupId1,patientSecondGroupId2,"
                    + "patientSecondGroupId3,patientSecondGroupId4)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            // create an insert sql statement
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Patient p = request.getPatient();
            st.setString(1, p.getFirstName());
            st.setString(2, p.getLastName());
            Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(p.getDOB());
            Timestamp ts = new Timestamp(date.getTime());
            st.setTimestamp(3, ts);
            st.setString(4, p.getGender());
            st.setFloat(5, p.getLeftArmLength());
            st.setFloat(6, p.getRightArmLength());
            st.setString(7, p.getHandDominance());
            st.setInt(8, request.getTherapistId());
            st.setInt(9, request.getLocationId());
            st.setInt(10, p.getPatientGroupId());
            st.setString(11, p.getCustomPatientId());
            st.setFloat(12, p.getRightStern2acJointLength());
            st.setFloat(13, p.getLeftStern2acJointLength());
            st.setFloat(14, p.getRightUpperArmLength());
            st.setFloat(15, p.getLeftUpperArmLength());
            st.setFloat(16, p.getHeight());
            st.setFloat(17, p.getWeight());
            st.setFloat(18, p.getRightForearmLength());
            st.setFloat(19, p.getLeftForearmLength());
            st.setString(20, p.getComments());
            st.setInt(21, p.getPatientSecondGroupId1());
            st.setInt(22, p.getPatientSecondGroupId2());
            st.setInt(23, p.getPatientSecondGroupId3());
            st.setInt(24, p.getPatientSecondGroupId4());
            
            
            st.executeUpdate();
            
            // get the auto generated session id to return to the client
            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            patientId = rs.getInt(1);
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
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
        response.setPatientId(patientId);
        response.setMsg("Patient added successfully");
        response.setRequestStatus(true);
        return gson.toJson(response);
    }
    
    
    public String getPatientInfo(PatientRequest request){
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        // only one record is expected, so no need to loop
        Patient p = new Patient();
        ServerResponse response = new ServerResponse();
        try{
            st = conn.createStatement();
            String sql = null;
            sql = "SELECT * FROM Patient WHERE patientId='" + request.getPatientId() + "'";
            rs = st.executeQuery(sql);
            if(!rs.next()){
                response.setMsg("Patient not found");
                response.setRequestStatus(false);
                return gson.toJson(response);
            }
            
            Date d = rs.getTimestamp("DOB");
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            String str = df.format(d);
            p.setDOB(str);
            p.setFirstName(rs.getString("firstName"));
            p.setLastName(rs.getString("lastName"));
            p.setGender(rs.getString("gender"));
            p.setHandDominance(rs.getString("handDominance"));
            p.setHeight(rs.getFloat("height"));
            p.setWeight(rs.getFloat("weight"));
            p.setPatientId(rs.getInt("patientId"));
            p.setPatientGroupId(rs.getInt("patientGroupId"));
            p.setTherapistId(rs.getInt("therapistId"));
            p.setLocationId(rs.getInt("locationId"));
            p.setRightStern2acJointLength(rs.getFloat("rightStern2acJointLength"));
            p.setLeftStern2acJointLength(rs.getFloat("leftStern2acJointLength"));
            p.setRightUpperArmLength(rs.getFloat("rightUpperArmLength"));
            p.setLeftUpperArmLength(rs.getFloat("leftUpperArmLength"));
            p.setRightForearmLength(rs.getFloat("rightForearmLength"));
            p.setLeftForearmLength(rs.getFloat("leftForearmLength"));
            p.setLeftArmLength(rs.getFloat("leftArmLength"));
            p.setRightArmLength(rs.getFloat("rightArmLength"));            
            p.setCustomPatientId(rs.getString("customPatientId"));
            p.setComments(rs.getString("notes"));
            p.setPatientSecondGroupId1(rs.getInt("patientSecondGroupId1"));
            p.setPatientSecondGroupId2(rs.getInt("patientSecondGroupId2"));
            p.setPatientSecondGroupId3(rs.getInt("patientSecondGroupId3"));
            p.setPatientSecondGroupId4(rs.getInt("patientSecondGroupId4"));
            
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
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
    
    public String editPatient(PatientRequest request){
        int patientId;
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
           
           
            String sql = "UPDATE `patient` SET `firstName`=?,`lastName`=?,"
                    + "`DOB`=?,`gender`=?,`therapistId`=?,"
                    + "`locationId`=?,"
                    + "`leftArmLength`=?,`rightArmLength`=?,"
                    + "`handDominance`=?,`patientGroupId`=?,"
                    + "`customPatientId`=?,`rightStern2acJointLength`=?,"
                    + "`leftStern2acJointLength`=?,`rightUpperArmLength`=?,"
                    + "`leftUpperArmLength`=?,`height`=?,"
                    + "`weight`=?,`rightForearmLength`=?,`leftForearmLength`=?,"
                    + "`notes`=?"
//                    + "`getPatientSecondGroupId1`=?, `getPatientSecondGroupId2`=?,"
 //                   + "`getPatientSecondGroupId3`=?, `getPatientSecondGroupId4`=?,"
                    + " WHERE `patientId`=?;";
            // create an insert sql statement
            st = conn.prepareStatement(sql);
            Patient p = request.getPatient();
            st.setString(1, p.getFirstName());
            st.setString(2, p.getLastName());
            if(p.getDOB() == null || p.getDOB().equals("")){
                throw new Exception("Missing Date of birth");
            }
            Date date = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).parse(p.getDOB());
            Timestamp ts = new Timestamp(date.getTime());
            st.setTimestamp(3, ts);
            st.setString(4, p.getGender());
            st.setInt(5, request.getTherapistId());
            st.setInt(6, request.getLocationId());
            st.setFloat(7, p.getLeftArmLength());
            st.setFloat(8, p.getRightArmLength());
            st.setString(9, p.getHandDominance());
            st.setInt(10, p.getPatientGroupId());
            st.setString(11, p.getCustomPatientId());
            st.setFloat(12, p.getRightStern2acJointLength());
            st.setFloat(13, p.getLeftStern2acJointLength());
            st.setFloat(14, p.getRightUpperArmLength());
            st.setFloat(15, p.getLeftUpperArmLength());
            st.setFloat(16, p.getHeight());
            st.setFloat(17, p.getWeight());
            st.setFloat(18, p.getRightForearmLength());
            st.setFloat(19, p.getLeftForearmLength());
            st.setString(20, p.getComments());
/*            st.setInt(21, p.getPatientSecondGroupId1());
            st.setInt(22, p.getPatientSecondGroupId2());
            st.setInt(23, p.getPatientSecondGroupId3());
            st.setInt(24, p.getPatientSecondGroupId4());*/
            st.setInt(21, p.getPatientId());
            st.executeUpdate();
            
        }catch(Exception e){
            e.printStackTrace();
            response.setMsg("Internal Server Error: " + e.getMessage());
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
        response.setMsg("Patient updated successfully");
        response.setRequestStatus(true);
        return gson.toJson(response);
    }
    
    
    public String getPatientList(PatientRequest request){
        Vector<Patient> list = new Vector<Patient>();
        Connection conn = DBConnection.getDBConnection();
        Statement st = null;
        ResultSet rs = null;
        Gson gson =  new Gson();
        ServerResponse response = new ServerResponse();
        PatientList pl = new PatientList();
        try{
            
            st = conn.createStatement();
            String sql = null;
            sql = "SELECT * FROM Patient WHERE patientGroupId=" +
                    request.getPatientGroupId() +
                    " AND locationId=" +
                    request.getLocationId() +
                    " ORDER BY customPatientId, lastName, firstName";
            rs = st.executeQuery(sql);
            if(!rs.next()){
                response.setMsg("No records found");
                response.setRequestStatus(false);
                return gson.toJson(response);
            }
            do{
                Patient p = new Patient();
                // we only need to return back combined first name and last name and patient id
                Date d = rs.getTimestamp("DOB");
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String str = df.format(d);
                p.setDOB(str);
                p.setFirstName(rs.getString("firstName"));
                p.setLastName(rs.getString("lastName"));
                p.setCustomPatientId(rs.getString("customPatientId"));
                p.setGender(rs.getString("gender"));
                p.setHandDominance(rs.getString("handDominance"));
                p.setLeftArmLength(rs.getFloat("leftArmLength"));
                p.setRightArmLength(rs.getFloat("leftArmLength"));
                p.setPatientId(rs.getInt("patientId"));
                list.add(p);
            }while(rs.next());
            rs.close();
            Patient[] pList = new Patient[list.size()];
            for(int i = 0; i < list.size(); i++){
                pList[i] = list.elementAt(i);
            }
            pl.setPatients(pList);
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
