/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.login;

/**
 *
 * @author Saqib
 */
public class LoginRequest {
    private String loginName;
    private String password;
    private String locationName;

    public String getLoginName() {
        return loginName;
    }

    public String getPassword() {
        return password;
    }
    
    public String getLocationName() {
        return locationName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    
}
