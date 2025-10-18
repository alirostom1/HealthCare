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
@Table(name = "responses")
@ToString(exclude = {"request"})
@NoArgsConstructor
@AllArgsConstructor
public class Response{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "request_id",nullable = false)
    private AsyncRequest request;

    @Column(nullable = false)
    private String response;

    @CreationTimestamp
    private LocalDateTime respondedAt;

    public String getFormattedRespondedAt(){
        return respondedAt != null ? respondedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) : "";
    }
    public Response(AsyncRequest request,String response){
        this.request = request;
        this.response = response;
    }
}
