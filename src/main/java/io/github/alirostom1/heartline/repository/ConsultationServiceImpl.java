package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.Generalist;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.ERole;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public class ConsultationServiceImpl implements ConsultationService{
    private final ConsultationRepo consultRepo;
    private final PatientRepo patientRepo;
    private final UserRepo userRepo;

    public ConsultationServiceImpl(ConsultationRepo consultRepo,PatientRepo patientRepo,UserRepo userRepo){
        this.consultRepo = consultRepo;
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Consultation createConsultation(UUID patientId, UUID generalistId, String motive, String observations) {
        try {
            Optional<User> optUser = userRepo.findById(generalistId);
            if (optUser.isEmpty()) {
                throw new RuntimeException("Generalist with such id not found: " + generalistId);
            }
            User user = optUser.get();
            if (user.getRole() != ERole.GENERALIST) {
                throw new RuntimeException("User found is not a generalist " + generalistId);
            }
            Optional<Patient> optPatient = patientRepo.findById(patientId);
            if (optPatient.isEmpty()) {
                throw new RuntimeException("Patient with such id not found: " + patientId);
            }
            Patient patient = optPatient.get();
            Generalist generalist = (Generalist) user;
            Consultation consultation = new Consultation(patient,generalist,motive,observations);
            patientRepo.removeFromQueue(patientId);
            return consultRepo.save(consultation);
        }catch(Exception e){
            throw e;
        }
    }
}
