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
public class DeleteEvaluationSessionRequest {
    int sessionId;
    int loginSessionId;

    public int getLoginSessionId() {
        return loginSessionId;
    }

    public void setLoginSessionId(int loginSessionId) {
        this.loginSessionId = loginSessionId;
    }
    
    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
    
}
