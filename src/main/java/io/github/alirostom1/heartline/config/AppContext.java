package io.github.alirostom1.heartline.config;

import io.github.alirostom1.heartline.service.UserService;
import jakarta.persistence.EntityManagerFactory;

public class AppContext {
    private final EntityManagerFactory emf;
    private final UserService userService;


    public AppContext(EntityManagerFactory emf,UserService userService){
        this.emf = emf;
        this.userService = userService;
    }
    public UserService getUserService(){
        return this.userService;
    }
    public void close(){
        if(emf != null && emf.isOpen()) emf.close();
    }
}
