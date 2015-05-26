/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.evaluations;

/**
 *
 * @author Gregorij
 */
public class TestCountResponse {
    private int testCountToday;
    private String testName;
    private boolean requestStatus;

    public int getTestCountToday() {
        return testCountToday;
    }

    public void setTestCountToday(int testCountToday) {
        this.testCountToday = testCountToday;
    }
    
    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }
    
    public boolean getRequestStatus() {
        return requestStatus;
    }
    
    public void setTestName(String testName) {
        this.testName = testName;
    }
    
    public String getTestName() {
        return testName;
    }
    
}
