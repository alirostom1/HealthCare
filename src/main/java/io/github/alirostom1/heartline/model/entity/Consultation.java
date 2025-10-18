package io.github.alirostom1.heartline.model.entity;


import com.mysql.cj.protocol.ColumnDefinition;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="consultations")
@ToString(exclude = {"patient","generalist","medicalActs","request"})
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "consultation_medical_acts",
        joinColumns = @JoinColumn(name = "consultation_id"),
        inverseJoinColumns = @JoinColumn(name = "medical_act_id")
    )
    private List<MedicalAct> medicalActs = new ArrayList<>();

    public double getTotalCost() {
        double medicalActsCost = medicalActs.stream()
                .mapToDouble(MedicalAct::getPrice).sum();
        return this.request == null ? this.cost + medicalActsCost : this.cost + medicalActsCost + this.request.getSpecialist().getFee();
    }
    public Long getCountMedicalActs(){
        Long count = medicalActs.stream().count();
        return count;
    }
    public double getMedicalActsCost(){
        return medicalActs.stream().mapToDouble(MedicalAct::getPrice).sum();
    }

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.OPEN;

    @OneToOne(mappedBy = "consultation",cascade = CascadeType.ALL)
    private Request request;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public String getFormattedCreatedAt(){
        return createdAt != null ? createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) : "";
    }

    public Consultation(Patient patient,Generalist generalist,String motive,String observations){
        this.patient = patient;
        this.generalist = generalist;
        this.motive = motive;
        this.observations = observations;
    }
}
