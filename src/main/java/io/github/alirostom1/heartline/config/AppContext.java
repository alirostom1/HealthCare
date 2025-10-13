package io.github.alirostom1.heartline.config;

import io.github.alirostom1.heartline.repository.ConsultationService;
import io.github.alirostom1.heartline.service.PatientService;
import io.github.alirostom1.heartline.service.UserService;
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



    public void close(){
        if(emf != null && emf.isOpen()) emf.close();
    }
}
