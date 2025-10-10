package io.github.alirostom1.heartline.config;

import io.github.alirostom1.heartline.service.PatientService;
import io.github.alirostom1.heartline.service.UserService;
import jakarta.persistence.EntityManagerFactory;

public class AppContext {
    private final EntityManagerFactory emf;
    private final UserService userService;
    private final PatientService patientService;


    public AppContext(EntityManagerFactory emf,UserService userService,PatientService patientService){
        this.emf = emf;
        this.userService = userService;
        this.patientService = patientService;
    }
    public UserService getUserService(){
        return this.userService;
    }
    public PatientService getPatientService(){return this.patientService;}
    public void close(){
        if(emf != null && emf.isOpen()) emf.close();
    }
}
