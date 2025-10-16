package io.github.alirostom1.heartline.config;

import io.github.alirostom1.heartline.job.GenerateWeeklyTimeSlots;
import io.github.alirostom1.heartline.model.entity.*;
import io.github.alirostom1.heartline.repository.*;
import io.github.alirostom1.heartline.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AppBootstraper implements ServletContextListener{
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce){
        ServletContext ctx = sce.getServletContext();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("healthcare_pu");
        UserRepo userRepo = new UserRepoImpl(emf);
        UserService userService = new UserServiceImpl(userRepo);
        PatientRepo patientRepo = new PatientRepoImpl(emf);
        PatientService patientService = new PatientServiceImpl(patientRepo);
        ConsultationRepo consultationRepo = new ConsultationRepoImpl(emf);
        ConsultationService consultationService = new ConsultationServiceImpl(consultationRepo,patientRepo,userRepo);
        SpecialistRepo specialistRepo = new SpecialistRepoImpl(emf);
        SpecialistService specialistService = new SpecialistServiceImpl(specialistRepo);
        TimeSlotRepo timeSlotRepo = new TimeSlotRepoImpl(emf);
        TimeSlotService timeSlotService = new TimeSlotServiceImpl(timeSlotRepo);
        AppContext appContext = new AppContext(emf,userService,patientService,consultationService,specialistService);
        ctx.setAttribute("appContext",appContext);


        GenerateWeeklyTimeSlots job1 = new GenerateWeeklyTimeSlots(timeSlotService,specialistService);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(job1,0,7, TimeUnit.DAYS);

    }
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        AppContext ctx = (AppContext) sce.getServletContext().getAttribute("appContext");
        if(ctx != null) ctx.close();
    }
    public void seed(EntityManagerFactory emf){
        MedicalAct medicalAct1 = new MedicalAct("X-ray", 250.0);
        MedicalAct medicalAct2 = new MedicalAct("Ultrasound", 300.0);
        MedicalAct medicalAct3 = new MedicalAct("MRI", 1200.0);
        MedicalAct medicalAct4 = new MedicalAct("Electrocardiogram", 150.0);
        MedicalAct medicalAct5 = new MedicalAct("Dermatology (Laser)", 500.0);
        MedicalAct medicalAct6 = new MedicalAct("Fundus examination", 200.0);
        MedicalAct medicalAct7 = new MedicalAct("Blood test", 100.0);
        MedicalAct medicalAct8 = new MedicalAct("Urine test", 80.0);
        List<MedicalAct> medicalActs = Arrays.asList(medicalAct1,medicalAct2,medicalAct3,medicalAct4,medicalAct5,medicalAct6,medicalAct7,medicalAct8);
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        medicalActs.forEach(em::persist);
        tx.commit();
        em.close();
    }

}
