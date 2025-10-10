package io.github.alirostom1.heartline.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "queue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Queue{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "patient_id",nullable = false,unique = true)
    private Patient patient;

    @CreationTimestamp
    @Column(name = "arrival_Time",nullable = false)
    private LocalDateTime arrivalTime;

    public Queue(Patient patient){
        this.patient = patient;
    }

}
