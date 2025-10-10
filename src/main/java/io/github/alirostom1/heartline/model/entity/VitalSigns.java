package io.github.alirostom1.heartline.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vitalsigns")
public class VitaSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;

    @Column(name = "body_temperature")
    private Double bodyTemperature; // Celsius

    @Column(name = "respiratory_rate")
    private Integer respiratoryRate; // breaths per minute

    private Double weight; // kg
    private Double height; // cm

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    

}
