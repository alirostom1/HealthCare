package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.TSstatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "time_slots")
public class TimeSlot{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "specialist_id",nullable = false)
    private Specialist specialist;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TSstatus status;

    public boolean isAvailable(){
        return status == TSstatus.AVAILABLE && startTime.isAfter(LocalDateTime.now());
    }
    public boolean isPast(){
        return endTime.isBefore(LocalDateTime.now());
    }

}
