package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.exception.PatientAlreadyQueued;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.VitalSigns;
import io.github.alirostom1.heartline.repository.PatientRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PatientServiceImpl implements PatientService{
    private final PatientRepo patientRepo;

    public PatientServiceImpl(PatientRepo patientRepo){
        this.patientRepo = patientRepo;
    }

    @Override
    public Patient registerPatient(Patient patient, VitalSigns vitalSigns) {
        Optional<Patient> existingPatient = patientRepo.findBySsn(patient.getSsn());
        if (existingPatient.isPresent()) {

            Patient updatedPatient = patientRepo.addVitalSigns(existingPatient.get().getId(), vitalSigns);
            patientRepo.addToQueue(updatedPatient.getId());
            return updatedPatient;
        } else {
            if(vitalSigns !=null){
                patient.getVitalSigns().add(vitalSigns);
                vitalSigns.setPatient(patient);
            }
            Patient savedPatient = patientRepo.save(patient);
            patientRepo.addToQueue(savedPatient.getId());
            return savedPatient;
        }
    }
    @Override
    public Patient updatePatient(Patient patient){
        return patientRepo.save(patient);
    }
    @Override
    public Optional<Patient> findPatientBySsn(String ssn) {
        return patientRepo.findBySsn(ssn);
    }
    @Override
    public List<Patient> findPatientsByFullName(String fullName){
        return patientRepo.findByFullName(fullName);
    }

    @Override
    public List<Patient> getTodayPatients() {
        return patientRepo.getTodayPatients();
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    @Override
    public List<Patient> getQueuePatients() {
        return patientRepo.getQueuePatients();
    }

    @Override
    public void addToQueue(UUID patientId) {
        try {
            patientRepo.addToQueue(patientId);
        } catch (PatientAlreadyQueued e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add patient to queue", e);
        }
    }

    @Override
    public void removeFromQueue(UUID patientId) {
        patientRepo.removeFromQueue(patientId);
    }

    @Override
    public Patient addVitalSigns(UUID patientId, VitalSigns vitalSigns) {
        return patientRepo.addVitalSigns(patientId,vitalSigns);
    }

    @Override
    public Optional<Patient> findPatientById(UUID id){
        return patientRepo.findById(id);
    }
}
