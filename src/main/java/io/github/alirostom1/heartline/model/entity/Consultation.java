package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="consultations")
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "generalist_id",nullable = false)
    private Generalist generalist;

    private String motive;
    private String observations;
    private double cost = 150.00;

    @ManyToMany
    @JoinTable(
        name = "consultation_medical_acts",
        joinColumns = @JoinColumn(name = "consultation_id"),
        inverseJoinColumns = @JoinColumn(name = "medical_act_id")
    )
    private List<MedicalAct> medicalActs = new ArrayList<>();

    public double getTotalCost() {
        double medicalActsCost = medicalActs.stream()
                .mapToDouble(MedicalAct::getPrice).sum();
        return this.cost + medicalActsCost;
    }

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.OPEN;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
