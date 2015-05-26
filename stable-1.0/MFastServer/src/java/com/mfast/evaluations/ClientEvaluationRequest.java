/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.evaluations;
/**
 *
 * @author Saqib
 */
public class ClientEvaluationRequest {
    public static final int TEST_REACHABLE_WS_RIGHT = 0;
    public static final int TEST_REACHABLE_WS_LEFT = 1;
    public static final int TEST_FUNCTIONAL_WS_RIGHT = 2;
    public static final int TEST_FUNCTIONAL_WS_LEFT = 3;
    public static final int TEST_SHOULDER_ROTATION_RIGHT = 4;
    public static final int TEST_SHOULDER_ROTATION_LEFT = 5;
   
    public static final int REQUEST_TYPE_RECORD = 1;
    public static final int REQUEST_TYPE_TEST = 0;
    
    public static final int REQUEST_TYPE_GET_COUNT = 2;
    
    int requestType;
    int patientId;
    float patientHeight;
    String patientGender;
    int testId;
    int level;
    int position;
    float weight;
    float clientVersion;
    int therapistId;
    int locationId;
    int loginSessionId;
    int showResults;
    private String optionalParameters;
    
    String dcAppServerIp = "localhost";    

    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }

    public int getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(int therapistId) {
        this.therapistId = therapistId;
    }
    
    public int getLocationId() {
        return this.locationId;
    }
    
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    public int getRequestType() {
        return requestType;
    }

    public int getPatientId() {
        return patientId;
    }
    
    public float getPatientHeight() {
        return patientHeight;
    }
    
    public String getPatientGender() {
        return patientGender;
    }

    public int getTestId() {
        return testId;
    }

    public int getLevel() {
        return level;
    }
    
    public int getPosition() {
        return position;
    }

    public float getWeight() {
        return weight;
    }

    public float getClientVersion() {
        return clientVersion;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public void setPatientHeight(float height) {
        this.patientHeight = height;
    }
    
    public void setPatientGender(String gender) {
        this.patientGender = gender;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setPosition(int position) {
        this.position = position;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setClientVersion(float clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setShowResults(int showResults) {
        this.showResults = showResults;
    }
    
    public int getShowResults() {
        return this.showResults;
    }
    
    public String getOptionalParameters() {
        return optionalParameters;
    }

    public void setOptionalParameters(String optionalParameters) {
        this.optionalParameters = optionalParameters;
    }
    
    public String getDCAppServerIp() {
        return this.dcAppServerIp;
    }
    
    public void setDCAppServerIp(String dcAppServerIp) {
        this.dcAppServerIp = dcAppServerIp;
    }
}
