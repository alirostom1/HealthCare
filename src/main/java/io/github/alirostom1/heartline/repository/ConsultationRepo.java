package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.MedicalAct;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepo extends CrudRepo<Consultation>{
    List<MedicalAct> getAllMedicalActs();
    Consultation addMedicalAct(UUID consultationId,UUID medicalActId);
    Consultation removeMedicalAct(UUID consultationId,UUID medicalActId);
}
