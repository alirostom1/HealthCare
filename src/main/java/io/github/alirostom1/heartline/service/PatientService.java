package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.VitalSigns;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {
    Patient registerPatient(Patient patient, VitalSigns vitalSigns);
    Patient updatePatient(Patient patient);
    Optional<Patient> findPatientBySsn(String ssn);
    List<Patient> findPatientsByFullName(String fullName);
    List<Patient> getTodayPatients();
    List<Patient> getAllPatients();

    List<Patient> getQueuePatients();
    void addToQueue(UUID patientId);
    void removeFromQueue(UUID patientId);

    Patient addVitalSigns(UUID patientId, VitalSigns vitalSigns);

    Optional<Patient> findPatientById(UUID id);
}
