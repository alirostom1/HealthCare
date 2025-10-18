package io.github.alirostom1.heartline.model.entity;

import io.github.alirostom1.heartline.model.enums.ERole;
import io.github.alirostom1.heartline.model.enums.Specialty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "specialists")
@DiscriminatorValue("specialist")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true,exclude = {"timeSlots","requests"})
@AllArgsConstructor
public class Specialist extends User{

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    private double fee;

    private int duration = 30;

    @OneToMany(mappedBy = "specialist",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @OrderBy("startTime desc")
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @OneToMany(mappedBy = "specialist",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Request> requests;

    public Specialist(){
        setRole(ERole.SPECIALIST);
    }
    public Specialist(String fullName, String username, String email,
                 String password) {
        super(fullName, username, email,password , ERole.SPECIALIST);
    }
    public Specialist(UUID id, String fullName, String username, String email,
                      String password, Specialty specialty, double fee, List<TimeSlot> timeSlots, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, fullName, username, email, password, ERole.SPECIALIST,createdAt,updatedAt);
        this.specialty = specialty;
        this.fee = fee;
        this.timeSlots = timeSlots;
    }
}
