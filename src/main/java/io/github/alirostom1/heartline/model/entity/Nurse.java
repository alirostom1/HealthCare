package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.ERole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "nurses")
@DiscriminatorValue("nurse")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Nurse extends User{

    public Nurse(){
        setRole(ERole.NURSE);
    }
    public Nurse(String fullName, String username, String email,
                 String password) {
        super(fullName, username, email,password , ERole.NURSE);
    }

    public Nurse(UUID id, String fullName, String username, String email,
                 String password, LocalDateTime createdAt,LocalDateTime updatedAt) {
        super(id, fullName, username, email, password, ERole.NURSE,createdAt,updatedAt);
    }
}
