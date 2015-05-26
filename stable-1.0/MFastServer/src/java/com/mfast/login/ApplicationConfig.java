/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mfast.login;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Saqib
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mfast.evaluations.CheckSessionStatusResource.class);
        resources.add(com.mfast.evaluations.DeleteEvaluationSessionResource.class);
        resources.add(com.mfast.evaluations.EvaluationRequestResource.class);
        resources.add(com.mfast.evaluations.PastResultsResource.class);
        resources.add(com.mfast.evaluations.UpdateEvaluationSessionResource.class);
        resources.add(com.mfast.login.TherapistLoginResource.class);
        resources.add(com.mfast.login.TherapistLogoutResource.class);
        resources.add(com.mfast.patient.PatientGroupManagerResource.class);
        resources.add(com.mfast.patient.PatientManagerResource.class);
    }
    
}
