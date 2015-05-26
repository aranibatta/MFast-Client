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
public class PatientGroupRequest {
    public static final int ADD_GROUP = 1;
    public static final int GET_LIST = 2;
    public static final int GET_GROUP_INFO = 3; 
    
    int requestType;
    PatientGroup[] groups;
    PatientGroup group;
    int loginSessionId;

    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }
    

    public PatientGroup getGroup() {
        return group;
    }

    public void setGroup(PatientGroup group) {
        this.group = group;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public PatientGroup[] getGroups() {
        return groups;
    }

    public void setGroups(PatientGroup[] groups) {
        this.groups = groups;
    }
    
}
