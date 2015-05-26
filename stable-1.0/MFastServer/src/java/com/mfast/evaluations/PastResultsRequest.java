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
public class PastResultsRequest {
    private int patientId;
    private String startDate;
    private String endDate;
    private int dataType;
    private int loginSessionId;
    

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }
    
}
