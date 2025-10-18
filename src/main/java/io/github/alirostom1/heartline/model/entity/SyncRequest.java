package io.github.alirostom1.heartline.model.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@ToString(callSuper = true, exclude = "timeSlot")
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("synchronized")
@AllArgsConstructor
@NoArgsConstructor
public class SyncRequest extends Request{

    @Column(columnDefinition = "TEXT")
    private String meetingUrl;

    @OneToOne
    @JoinColumn(name = "timeslot_id")
    private TimeSlot timeSlot;

    public SyncRequest(Consultation consultation,Specialist specialist,TimeSlot ts,String url){
        super(consultation,specialist);
        this.meetingUrl = url;
        this.timeSlot = ts;
    }
}
