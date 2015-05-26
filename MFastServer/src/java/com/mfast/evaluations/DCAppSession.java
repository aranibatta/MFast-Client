/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.evaluations;

import com.mfast.common.DBConnection;
import com.mfast.dcajson.Recording;
import com.mfast.dcajson.Skeleton;
import com.mfast.dcajson.SkeletonSequence;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author Saqib
 */
public class DCAppSession implements Runnable{
    ClientEvaluationRequest request;
    int sessionId;
    Connection conn = null;
    PreparedStatement st = null;
    
    public DCAppSession(ClientEvaluationRequest r, int sId){
        this.request = r;
        this.sessionId = sId;
    }
    
    
    /**
     * Request send to DCApp
     * @return
     * @throws Exception 
     */
    public String sendGet() throws Exception {
        String url = null;
        //StringBuilder urlSB = new StringBuilder("http://localhost:55780/runtests?testid=");
        StringBuilder urlSB = new StringBuilder("http://");
        urlSB.append(request.getDCAppServerIp());
        urlSB.append(":55780/runtests?testid=");
        urlSB.append(request.getTestId());
        urlSB.append("&level=");
        urlSB.append(request.getLevel());
        urlSB.append("&position=");
        urlSB.append(request.getPosition());
        urlSB.append("&patientId=");
        urlSB.append(request.getPatientId());
        urlSB.append("&patientHeight=");
        urlSB.append(request.getPatientHeight());
        urlSB.append("&patientGender=");
        urlSB.append(request.getPatientGender());
        urlSB.append("&sessionId=");
        urlSB.append(sessionId);
        urlSB.append("&weight=");
        urlSB.append(request.getWeight());
        urlSB.append("&showResults=");
        urlSB.append(request.getShowResults());
        url = urlSB.toString();
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");
        
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        String msg = con.getResponseMessage();
        System.out.println("response message " + msg);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
    
    private void updateSessionWithFrameNumber(int frameNumber) throws Exception{
        
        st = conn.prepareStatement("UPDATE MeasurementSession SET frameCount=? where measurementSessionId=?");
        st.setInt(1, frameNumber);
        st.setInt(2, sessionId);
        st.executeUpdate();
    }
    
    private String sendGetDummy(){
        String url = "file:///C:/Users/Saqib/Dropbox/Projects/ActiveProjects/FivePlus/DataCollectionApps-12-19-2013/bin/json_output.txt";
        String contents = null;
        Gson gson = new Gson();
        try{
            contents = IOUtils.toString(new URL(url));
            
        }catch(IOException e){
            e.printStackTrace();
        }
        return contents;
    }
    
    private int putSkeletonInDB(int measurementFrameId, Skeleton sk) throws Exception{
        int skeletonId = 0;
        StringBuilder sq = new StringBuilder("INSERT INTO Skeleton ");
        sq.append("(measurementFrameId, skeletonSequenceNumber, ");
        sq.append("headX, headY, headZ, ");
        sq.append("hipCenterX, hipCenterY, hipCenterZ, ");
        sq.append("leftAnkleX, leftAnkleY, leftAnkleZ, ");
        sq.append("rightAnkleX, rightAnkleY, rightAnkleZ, ");
        sq.append("leftElbowX, leftElbowY, leftElbowZ, ");
        sq.append("rightElbowX, rightElbowY, rightElbowZ, ");
        sq.append("leftFootX, leftFootY, leftFootZ, ");
        sq.append("rightFootX, rightFootY, rightFootZ, ");
        sq.append("leftHandX, leftHandY, leftHandZ, ");
        sq.append("rightHandX, rightHandY, rightHandZ, ");
        sq.append("leftHipX, leftHipY, leftHipZ, ");
        sq.append("rightHipX, rightHipY, rightHipZ, ");
        sq.append("leftKneeX, leftKneeY, leftKneeZ, ");
        sq.append("rightKneeX, rightKneeY, rightKneeZ, ");
        sq.append("leftShoulderX, leftShoulderY, leftShoulderZ, ");
        sq.append("rightShoulderX, rightShoulderY, rightShoulderZ, ");
        sq.append("leftWristX, leftWristY, leftWristZ, ");
        sq.append("rightWristX, rightWristY, rightWristZ, ");
        sq.append("shoulderCenterX, shoulderCenterY, shoulderCenterZ, ");
        sq.append("spineX, spineY, spineZ) ");
        // 62 values
        sq.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");
        
        st = conn.prepareStatement(sq.toString(), Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, measurementFrameId);
        st.setInt(2, sk.getId());
        st.setDouble(3, sk.getHead()[0]); st.setDouble(4, sk.getHead()[1]); st.setDouble(5, sk.getHead()[2]);
        st.setDouble(6, sk.getHipCenter()[0]); st.setDouble(7, sk.getHipCenter()[1]); st.setDouble(8, sk.getHipCenter()[2]);
        st.setDouble(9, sk.getLeftAnkle()[0]); st.setDouble(10, sk.getLeftAnkle()[1]); st.setDouble(11, sk.getLeftAnkle()[2]);
        st.setDouble(12, sk.getRightAnkle()[0]); st.setDouble(13, sk.getRightAnkle()[1]); st.setDouble(14, sk.getRightAnkle()[2]);
        st.setDouble(15, sk.getLeftElbow()[0]); st.setDouble(16, sk.getLeftElbow()[1]); st.setDouble(17, sk.getLeftElbow()[2]);
        st.setDouble(18, sk.getRightElbow()[0]); st.setDouble(19, sk.getRightElbow()[1]); st.setDouble(20, sk.getRightElbow()[2]);
        st.setDouble(21, sk.getLeftFoot()[0]); st.setDouble(22, sk.getLeftFoot()[1]); st.setDouble(23, sk.getLeftFoot()[2]);
        st.setDouble(24, sk.getRightFoot()[0]); st.setDouble(25, sk.getRightFoot()[1]); st.setDouble(26, sk.getRightFoot()[2]);
        st.setDouble(27, sk.getLeftHand()[0]); st.setDouble(28, sk.getLeftHand()[1]); st.setDouble(29, sk.getLeftHand()[2]);
        st.setDouble(30, sk.getRightHand()[0]); st.setDouble(31, sk.getRightHand()[1]); st.setDouble(32, sk.getRightHand()[2]);
        st.setDouble(33, sk.getLeftHip()[0]); st.setDouble(34, sk.getLeftHip()[1]); st.setDouble(35, sk.getLeftHip()[2]);
        st.setDouble(36, sk.getRightHip()[0]); st.setDouble(37, sk.getRightHip()[1]); st.setDouble(38, sk.getRightHip()[2]);
        st.setDouble(39, sk.getLeftKnee()[0]); st.setDouble(40, sk.getLeftKnee()[1]); st.setDouble(41, sk.getLeftKnee()[2]);
        st.setDouble(42, sk.getRightKnee()[0]); st.setDouble(43, sk.getRightKnee()[1]); st.setDouble(44, sk.getRightKnee()[2]);
        st.setDouble(45, sk.getLeftShoulder()[0]); st.setDouble(46, sk.getLeftShoulder()[1]); st.setDouble(47, sk.getLeftShoulder()[2]);
        st.setDouble(48, sk.getRightShoulder()[0]); st.setDouble(49, sk.getRightShoulder()[1]); st.setDouble(50, sk.getRightShoulder()[2]);
        st.setDouble(51, sk.getLeftWrist()[0]); st.setDouble(52, sk.getLeftWrist()[1]); st.setDouble(53, sk.getLeftWrist()[2]);
        st.setDouble(54, sk.getRightWrist()[0]); st.setDouble(55, sk.getRightWrist()[1]); st.setDouble(56, sk.getRightWrist()[2]);
        st.setDouble(57, sk.getShoulderCenter()[0]); st.setDouble(58, sk.getShoulderCenter()[1]); st.setDouble(59, sk.getShoulderCenter()[2]);
        st.setDouble(60, sk.getSpine()[0]); st.setDouble(61, sk.getSpine()[1]); st.setDouble(62, sk.getSpine()[2]);
        
        st.executeUpdate();
        
        // get the auto generated session id to return to the client
        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        skeletonId = rs.getInt(1);
        rs.close();
        
        return skeletonId;
    }
    
    
    
    private void putFrameInDB(SkeletonSequence frame) throws Exception{
        int measurementFrameId = 0;
        String sql = "INSERT INTO MeasurementFrame (frameNumber, frameTime, realTimeStampSeconds, realTimeStampMicros, skeletonCount, measurementSessionId) VAlUES(?,?,?,?,?,?)";
        // first create a new frame entry
        st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setInt(1, frame.getFrameNumber());
        st.setFloat(2, frame.getTimeStamp());
        st.setInt(3, frame.getRealTimeStampSeconds());
        st.setInt(4, frame.getRealTimeStampMicros());
        st.setInt(5, frame.getSkeletonCount());
        st.setInt(6, sessionId);

        // set the timestamp (this is currnet time when putting it into database!)
/*        java.util.Date date = new java.util.Date();
        Timestamp ts = new Timestamp(date.getTime());
        st.setTimestamp(7, ts);*/
        st.executeUpdate();
        
        // get the auto generated session id to return to the client
        ResultSet rs = st.getGeneratedKeys();
        rs.next();
        measurementFrameId = rs.getInt(1);
        rs.close();
        
        if(frame.getSkeletons() == null){
            return;
        }
        
        for(Skeleton sk : frame.getSkeletons()){
            this.putSkeletonInDB(measurementFrameId, sk);
        }
    }
    
    private void setEvalationSessionCompleted(String status) throws Exception{
        // session complete, update the session status in DB
        st = conn.prepareStatement("UPDATE MeasurementSession SET isCompleted=?, sessionStatus=? where measurementSessionId=?");
        st.setBoolean(1, true);
        st.setString(2, status);
        st.setInt(3, sessionId);
        st.executeUpdate();
    }
    
    public void run(){
        try{
            
            String DCAppResponse = sendGet();
            Gson gson = new Gson();
            
            // get a database connection
            conn = DBConnection.getDBConnection();
            if(DCAppResponse.length() == 0){
                setEvalationSessionCompleted("Data Collection Failed");
                return;
            }
            Recording res = gson.fromJson(DCAppResponse, Recording.class);
            if(res.getFrameCount() <= 0){
                // error collecting data
                setEvalationSessionCompleted("Data Collection Failed");
                return;
            }
            
            if(request.requestType == request.REQUEST_TYPE_TEST){
                setEvalationSessionCompleted("Test Session Only. No Recording");
                return;
            }
            conn.setAutoCommit(false);
            
            // update the frame count in the session
            this.updateSessionWithFrameNumber(res.getFrameCount());
            
            if(res.getSkeletonSequence() != null){
                for(SkeletonSequence seq: res.getSkeletonSequence()){
                    this.putFrameInDB(seq);
                }
            }
            setEvalationSessionCompleted("Evaluation data recording complete");
            
            conn.commit();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
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
    }
    
    public DCAppSession(){this.sessionId = 4;}
    
    public static void main(String[] args){
        new DCAppSession().run();
    }
}
