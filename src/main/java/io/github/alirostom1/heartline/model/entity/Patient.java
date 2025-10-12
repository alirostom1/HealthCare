package io.github.alirostom1.heartline.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
@ToString(exclude = {"vitalSigns"})
public class Patient{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "ssn",unique = true, nullable = false)
    private String ssn;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "patient",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    private List<VitalSigns> vitalSigns = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Patient(String firstName, String lastName, LocalDate birthDate, String ssn, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
    }
}
