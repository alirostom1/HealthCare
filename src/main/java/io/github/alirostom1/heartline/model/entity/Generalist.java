package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.ERole;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "generalists")
@DiscriminatorValue("generalist")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Generalist extends User{
    public Generalist(){
        setRole(ERole.GENERALIST);
    }
    public Generalist(String fullName, String username, String email,
                 String password) {
        super(fullName, username, email,password , ERole.GENERALIST);
    }

    public Generalist(UUID id, String fullName, String username, String email,
                 String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, fullName, username, email, password, ERole.GENERALIST,createdAt,updatedAt);
    }
}
