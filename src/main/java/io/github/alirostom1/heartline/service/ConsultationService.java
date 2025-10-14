package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.MedicalAct;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultationService {
    Consultation createConsultation(UUID patientId, UUID generalistId, String motive, String observations);

    List<Consultation> getAllConsultations();

    Optional<Consultation> getConsultationById(UUID id);

    List<MedicalAct> getAllMedicalActs();
    List<MedicalAct> getAllAvailableMedicalActs(UUID consultationId);

    Consultation addMedicalAct(UUID consultationId,UUID medicalActId);

    Consultation removeMedicalAct(UUID consultationId,UUID medicalActId);
    Consultation updateStatus(UUID consultationId, ConsultationStatus status);

    Consultation updateDiagnosis(UUID consultationId,String diagnosis);
    Consultation updateTreatment(UUID consultationId,String treatment);
}
