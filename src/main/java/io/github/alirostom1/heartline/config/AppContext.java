package io.github.alirostom1.heartline.config;

import io.github.alirostom1.heartline.service.*;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppContext {
    private final EntityManagerFactory emf;
    private final UserService userService;
    private final PatientService patientService;
    private final ConsultationService consultationService;
    private final SpecialistService specialistService;
    private final RequestService requestService;
    private final TimeSlotService timeSlotService;


    public void close(){
        if(emf != null && emf.isOpen()) emf.close();
    }
}
