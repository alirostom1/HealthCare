package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.repository.PatientRepo;
import io.github.alirostom1.heartline.repository.PatientRepoImpl;
import io.github.alirostom1.heartline.repository.UserRepo;
import io.github.alirostom1.heartline.repository.UserRepoImpl;
import io.github.alirostom1.heartline.service.PatientService;
import io.github.alirostom1.heartline.service.PatientServiceImpl;
import io.github.alirostom1.heartline.service.UserService;
import io.github.alirostom1.heartline.service.UserServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppBootstraper implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent sce){
        ServletContext ctx = sce.getServletContext();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("healthcare_pu");
        UserRepo userRepo = new UserRepoImpl(emf);
        UserService userService = new UserServiceImpl(userRepo);
        PatientRepo patientRepo = new PatientRepoImpl(emf);
        PatientService patientService = new PatientServiceImpl(patientRepo);
        AppContext appContext = new AppContext(emf,userService,patientService);
        ctx.setAttribute("appContext",appContext);
    }
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        AppContext ctx = (AppContext) sce.getServletContext().getAttribute("appContext");
        if(ctx != null) ctx.close();
    }
}
