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
public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private String DOB;
    private String gender;
    private String handDominance;
    private int therapistId;
    private int patientGroupId;
    private int locationId;
    private String customPatientId;
    private float rightStern2acJointLength;
    private float leftStern2acJointLength; 
    private float rightUpperArmLength;
    private float leftUpperArmLength;
    private float rightForearmLength;
    private float leftForearmLength;
    private float rightArmLength;
    private float leftArmLength;
    private float height;
    private float weight;
    private String comments;
    private int patientSecondGroupId1;
    private int patientSecondGroupId2;
    private int patientSecondGroupId3;
    private int patientSecondGroupId4;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getCustomPatientId() {
        return customPatientId;
    }

    public void setCustomPatientId(String customPatientId) {
        this.customPatientId = customPatientId;
    }
    
    public int getPatientGroupId() {
        return patientGroupId;
    }

    public void setPatientGroupId(int patientGroupId) {
        this.patientGroupId = patientGroupId;
    }

    public int getLocationId() {
        return this.locationId;
    }
    
    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
    
    public int getTherapistId() {
        return therapistId;
    }

    public void setTherapistId(int therapistId) {
        this.therapistId = therapistId;
    }
    
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getLeftArmLength() {
        return leftArmLength;
    }

    public void setLeftArmLength(float leftArmLength) {
        this.leftArmLength = leftArmLength;
    }

    public float getRightArmLength() {
        return rightArmLength;
    }

    public void setRightArmLength(float rightArmLength) {
        this.rightArmLength = rightArmLength;
    }

    public String getHandDominance() {
        return handDominance;
    }

    public void setHandDominance(String handDominance) {
        this.handDominance = handDominance;
    }

    public float getRightStern2acJointLength() {
        return rightStern2acJointLength;
    }

    public void setRightStern2acJointLength(float rightStern2acJointLength) {
        this.rightStern2acJointLength = rightStern2acJointLength;
    }

    public float getLeftStern2acJointLength() {
        return leftStern2acJointLength;
    }

    public void setLeftStern2acJointLength(float leftStern2acJointLength) {
        this.leftStern2acJointLength = leftStern2acJointLength;
    }

    public float getRightUpperArmLength() {
        return rightUpperArmLength;
    }

    public void setRightUpperArmLength(float rightUpperArmLength) {
        this.rightUpperArmLength = rightUpperArmLength;
    }

    public float getLeftUpperArmLength() {
        return leftUpperArmLength;
    }

    public void setLeftUpperArmLength(float leftUpperArmLength) {
        this.leftUpperArmLength = leftUpperArmLength;
    }


    public float getRightForearmLength() {
        return rightForearmLength;
    }

    public void setRightForearmLength(float rightForearmLength) {
        this.rightForearmLength = rightForearmLength;
    }

    public float getLeftForearmLength() {
        return leftForearmLength;
    }

    public void setLeftForearmLength(float leftForearmLength) {
        this.leftForearmLength = leftForearmLength;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    
    public int getPatientSecondGroupId1() {
        return patientSecondGroupId1;
    }

    public void setPatientSecondGroupId1(int patientSecondGroupId1) {
        this.patientSecondGroupId1 = patientSecondGroupId1;
    }
    
    public int getPatientSecondGroupId2() {
        return patientSecondGroupId2;
    }

    public void setPatientSecondGroupId2(int patientSecondGroupId2) {
        this.patientSecondGroupId2 = patientSecondGroupId2;
    }    
    
    public int getPatientSecondGroupId3() {
        return patientSecondGroupId3;
    }

    public void setPatientSecondGroupId3(int patientSecondGroupId3) {
        this.patientSecondGroupId3 = patientSecondGroupId3;
    }
    
    public int getPatientSecondGroupId4() {
        return patientSecondGroupId4;
    }

    public void setPatientSecondGroupId4(int patientSecondGroupId4) {
        this.patientSecondGroupId4 = patientSecondGroupId4;
    }
    
}
