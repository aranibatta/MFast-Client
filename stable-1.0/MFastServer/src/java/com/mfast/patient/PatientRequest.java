/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.patient;

/**
 *
 * @author Saqib
 */
public class PatientRequest {
    public static final int ADD_PATIENT = 1;
    public static final int GET_PATIENT_LIST = 2;
    public static final int GET_PATIENT_INFO = 3;
    public static final int EDIT_PATIENT_INFO = 4;
    
    Patient patient;
    PatientList patientList;
    int requestType;
    int therapistId;
    int patientId;
    int locationId;
    int patientGroupId;
    int loginSessionId;

    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }
    
    public int getPatientGroupId() {
        return patientGroupId;
    }

    public void setPatientGroupId(int patientGroupId) {
        this.patientGroupId = patientGroupId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public PatientList getPatientList() {
        return patientList;
    }

    public void setPatientList(PatientList patientList) {
        this.patientList = patientList;
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
}
