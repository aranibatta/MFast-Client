/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.common;

/**
 *
 * @author saqibahmad
 */
public class ServerResponse {
    private boolean requestStatus;
    private String msg;
    private int therapistId;
    private String therapistFirstName;
    private String therapistLastName;
    private int patientId;
    private int patientGroupId;
    private int sessionId;
    private int errorId;
    private int loginSessionId;
    private int locationId;
    private String locationName;

    public int getPatientGroupId() {
        return patientGroupId;
    }

    public void setPatientGroupId(int patientGroupId) {
        this.patientGroupId = patientGroupId;
    }

    
    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }

    public String getTherapistFirstName() {
        return therapistFirstName;
    }

    public void setTherapistFirstName(String therapistFirstName) {
        this.therapistFirstName = therapistFirstName;
    }

    public String getTherapistLastName() {
        return therapistLastName;
    }

    public void setTherapistLastName(String therapistLastName) {
        this.therapistLastName = therapistLastName;
    }
    
    public int getErrorId() {
        return errorId;
    }

    public void setErrorId(int errorId) {
        this.errorId = errorId;
    }
    
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
    public void setTherapistId(int therapistId) {
        this.therapistId = therapistId;
    }

    public int getTherapistId() {
        return therapistId;
    }

    public boolean isRequestStatus() {
        return requestStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public int getLocationId() {
        return this.locationId;
    }
    
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    public String getLocationName() {
        return this.locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
  
    
}
