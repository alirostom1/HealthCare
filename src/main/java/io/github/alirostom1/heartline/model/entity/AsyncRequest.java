package io.github.alirostom1.heartline.model.entity;


import io.github.alirostom1.heartline.model.enums.RequestPrio;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true,exclude = "response")
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("asynchronized")
public class AsyncRequest extends Request{
    @Column(columnDefinition = "TEXT")
    private String question;

    @OneToOne(mappedBy = "request",cascade = CascadeType.ALL)
    private Response response;

    public AsyncRequest(Consultation consultation, Specialist specialist, RequestPrio prio,String question){
        super(consultation,specialist,prio);
        this.question = question;
    }
}
