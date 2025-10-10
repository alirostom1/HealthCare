package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.VitalSigns;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepo extends CrudRepo<Patient>{
    Optional<Patient> findBySsn(String ssn);
    List<Patient> findByFullName(String fullName);
    List<Patient> getTodayPatients();

    Patient addVitalSigns(UUID patientId, VitalSigns vitalSigns);

    void addToQueue(UUID patientId);
    void removeFromQueue(UUID patientId);
    List<Patient> getQueuePatients();
}
