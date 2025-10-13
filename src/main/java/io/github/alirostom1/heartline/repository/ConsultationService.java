package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Consultation;

import java.util.UUID;

public interface ConsultationService {
    Consultation createConsultation(UUID patientId,UUID generalistId,String motive,String observations);

}
