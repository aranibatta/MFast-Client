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
public class PatientGroup {
    int patientGroupId;
    String groupName;
    String groupDescription;

    public int getPatientGroupId() {
        return patientGroupId;
    }

    public void setPatientGroupId(int patientGroupId) {
        this.patientGroupId = patientGroupId;
    }

        
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

}
