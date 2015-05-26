package com.mfast.login;

import com.mfast.common.DBConnection;
import com.mfast.common.ServerResponse;
import com.mfast.common.Util;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Saqib
 */
public class LoginManager {
    
    private static int createLoginSession(Connection conn, int therapistId, int locationId) throws Exception{
        int sessionId = 0;                                                          
        String sql = "INSERT INTO LoginSession(LoggedInTherapist, LoginSessionActive, LoginSessionLastActivityTS, LoggedInLocation) VALUES(?,?,?,?)";
        
        // create
        PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, therapistId);
        st.setBoolean(2, true);
        st.setTimestamp(3, Util.getTS());
        st.setInt(4, locationId);
        
        st.executeUpdate();

        // get the auto generated session id to return to the client
        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        sessionId = rs.getInt(1);
        rs.close();
        
        return sessionId;
    }
    
    public static String loginTherapist(LoginRequest request) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        Connection conn = null;
        ServerResponse response = new ServerResponse();
        Gson gson = new Gson();
        conn = DBConnection.getDBConnection();
        if(conn == null){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return gson.toJson(response);
        }
        try{
            st = conn.createStatement();
            String sql;
            
            // Check username/password
            sql = "SELECT * FROM Therapist where userName='" + request.getLoginName() + "'";
            rs = st.executeQuery(sql);
            if(!rs.next()){
                response.setMsg("Incorrect user name or password");
                response.setRequestStatus(false);
                return gson.toJson(response);
            }
            // do hash of the provided password
            //MessageDigest sha = MessageDigest.getInstance("SHA-256");
            //byte[] pwdHashBytes = sha.digest(request.getPassword().getBytes());
            //String providedPassword = new String(pwdHashBytes);
            if(request.getPassword().equals(rs.getString("password"))){
                
                // passwords match
                response.setMsg("Success");
                response.setRequestStatus(true);
                int therapistId = rs.getInt("therapistId");
                response.setTherapistId(therapistId);
                response.setTherapistFirstName(rs.getString("firstName"));
                response.setTherapistLastName(rs.getString("lastName"));
                //response.setLoginSessionId(LoginManager.createLoginSession(conn, therapistId));
                
                // Check the site ID 
                sql = "SELECT * FROM Location where locationName='" + request.getLocationName() + "'";
                rs = st.executeQuery(sql);
                if(!rs.next()){
                    response.setMsg("Incorrect location id.");
                    response.setRequestStatus(false);
                    return gson.toJson(response);
                }
                int locationId = rs.getInt("locationId");
                // TODO: Check whether ID of therapist matches location Id
                response.setLocationId(locationId);
                response.setLocationName(request.getLocationName());
                
                // save login session
                response.setLoginSessionId(LoginManager.createLoginSession(conn, therapistId, locationId));
                
            }else{
                response.setMsg("Incorrect user name or password");
                response.setRequestStatus(false);
            }
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
        return gson.toJson(response);
    }
    
    public static String logoutTherapist(LogoutRequest request) throws Exception{
        PreparedStatement st = null;
        Connection conn = null;
        ServerResponse response = new ServerResponse();
        Gson gson = new Gson();
        conn = DBConnection.getDBConnection();
        if(conn == null){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return gson.toJson(response);
        }
        try{
            String sql;
            sql = "UPDATE LoginSession SET LoginSessionActive=?, LoginSessionLastActivityTS=? where loginSessionId=" + request.getLoginSessionId()+";";
            st = conn.prepareStatement(sql);
            st.setBoolean(1, false);
            st.setTimestamp(2, Util.getTS());
            
            st.executeUpdate();
            response.setMsg("User logged out successfully");
            response.setRequestStatus(true);
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
        return gson.toJson(response);
    }
    
    public static boolean verifyLoginSession(int sessionId, ServerResponse response){
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection conn = null;
        conn = DBConnection.getDBConnection();
        boolean isActive = false;
        if(conn == null){
            response.setMsg("Internal Server Error");
            response.setRequestStatus(false);
            return false;
        }
        try{
            String sql;
            // get the login session information
            sql = "SELECT * from LoginSession WHERE loginSessionId=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, sessionId);
            rs = st.executeQuery();
            if(!rs.next()){
                response.setMsg("Invalid session. Therapist must login");
                response.setRequestStatus(false);
                return false;
            }
            
            isActive = rs.getBoolean("LoginSessionActive");
            rs.close();
            
            sql = "UPDATE LoginSession SET LoginSessionLastActivityTS=? where loginSessionId=?;";
            st = conn.prepareStatement(sql);
            st.setTimestamp(1, Util.getTS());
            st.setInt(2, sessionId);
            
            st.executeUpdate();
 
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                if (st != null) {
                    st.close();
                }
                conn.close();
            } catch (SQLException se2) {
            }// nothing we can do
        }
        if(!isActive){
            response.setMsg("Login session not active");
            response.setRequestStatus(false);
        }
        return isActive;
    } 
}
