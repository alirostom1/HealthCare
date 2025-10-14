package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.*;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import io.github.alirostom1.heartline.model.enums.ERole;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Override
    public List<Consultation> getAllConsultations(){
        return consultRepo.findAll();
    }
    @Override
    public Optional<Consultation> getConsultationById(UUID id){
        return consultRepo.findById(id);
    }

    @Override
    public List<MedicalAct> getAllMedicalActs() {
        return consultRepo.getAllMedicalActs();
    }

    @Override
    public List<MedicalAct> getAllAvailableMedicalActs(UUID consultationId) {
        Optional<Consultation> optionalConsultation = consultRepo.findById(consultationId);
        if(optionalConsultation.isPresent()){
            List<MedicalAct> allMedicalActs = consultRepo.getAllMedicalActs();
            List<MedicalAct> existingMedicalActs = optionalConsultation.get().getMedicalActs();
            return allMedicalActs.stream().filter(ma -> !existingMedicalActs.contains(ma)).collect(Collectors.toList());
        }
        return consultRepo.getAllMedicalActs();
    }
    @Override
    public Consultation addMedicalAct(UUID consultationId,UUID medicalActId){
        return consultRepo.addMedicalAct(consultationId,medicalActId);
    }
    @Override
    public Consultation removeMedicalAct(UUID consultationId,UUID medicalActID){
        return consultRepo.removeMedicalAct(consultationId,medicalActID);
    }
    @Override
    public Consultation updateStatus(UUID consultationId, ConsultationStatus status){
        Optional<Consultation> optConsultation = consultRepo.findById(consultationId);
        if(optConsultation.isEmpty()){
            throw new IllegalArgumentException("Consultation not found with id : " + consultationId);
        }
        Consultation consultation = optConsultation.get();
        consultation.setStatus(status);
        return consultRepo.save(consultation);
    }
    @Override
    public Consultation updateDiagnosis(UUID consultationId,String diagnosis){
        Optional<Consultation> optionalConsultation = consultRepo.findById(consultationId);
        if(optionalConsultation.isEmpty()){
            throw new RuntimeException("Consultation not found with id: " + consultationId);
        }
        Consultation consultation = optionalConsultation.get();
        consultation.setDiagnosis(diagnosis);
        return consultRepo.save(consultation);
    }
    @Override
    public Consultation updateTreatment(UUID consultationId,String treatment){
        Optional<Consultation> optionalConsultation = consultRepo.findById(consultationId);
        if(optionalConsultation.isEmpty()){
            throw new RuntimeException("Consultation not found with id: " + consultationId);
        }
        Consultation consultation = optionalConsultation.get();
        consultation.setTreatment(treatment);
        return consultRepo.save(consultation);
    }



}
