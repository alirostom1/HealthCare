package io.github.alirostom1.heartline.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vitalsigns")
@ToString(exclude = {"patient"})
public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @Column(name = "blood_pressure")
    private String bloodPressure;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "body_temperature")
    private Double bodyTemperature;

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate;

    private Double weight; // kg
    private Double height; // cm

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public VitalSigns(Patient patient, String bloodPressure, Integer heartRate,
                      Double bodyTemperature, Integer respiratoryRate, Double weight, Double height) {
        this.patient = patient;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.bodyTemperature = bodyTemperature;
        this.respiratoryRate = respiratoryRate;
        this.weight = weight;
        this.height = height;
    }

}
