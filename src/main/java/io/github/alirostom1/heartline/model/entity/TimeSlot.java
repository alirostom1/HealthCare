package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.TSstatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Data
@Table(name = "time_slots")
@ToString(exclude = {"specialist","request"})
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "specialist_id",nullable = false)
    private Specialist specialist;

    @JoinColumn(nullable = false)
    private LocalDateTime startTime;
    @JoinColumn(nullable = false)
    private LocalDateTime endTime;

    public String getFormattedStartTime(){
        return this.startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    public String getFormattedEndTime(){
        return this.endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    public String getFormattedDate(){
        return this.startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Enumerated(EnumType.STRING)
    private TSstatus status = TSstatus.AVAILABLE;

    @OneToOne(mappedBy = "timeSlot",cascade = CascadeType.ALL)
    private SyncRequest request;

    public boolean isAvailable(){
        return status == TSstatus.AVAILABLE && startTime.isAfter(LocalDateTime.now());
    }
    public boolean isPast(){
        return endTime.isBefore(LocalDateTime.now());
    }

    public TimeSlot(Specialist specialist,LocalDateTime startTime,LocalDateTime endTime){
        this.specialist = specialist;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
