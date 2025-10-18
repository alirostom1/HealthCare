    package io.github.alirostom1.heartline.model.entity;

    import io.github.alirostom1.heartline.model.enums.RequestPrio;
    import io.github.alirostom1.heartline.model.enums.RequestStatus;
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
    @Table(name = "requests")
    @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
    @DiscriminatorColumn(name = "request_type",discriminatorType = DiscriminatorType.STRING)
    @ToString(exclude = {"consultation","specialist"})
    @AllArgsConstructor
    @NoArgsConstructor
    public abstract class Request{
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private RequestPrio requestPrio = RequestPrio.NORMAL;

        @Enumerated(EnumType.STRING)
        private RequestStatus status = RequestStatus.WAITING;


        @OneToOne
        @JoinColumn(name = "consultation_id",nullable = false,unique = true)
        private Consultation consultation;

        @ManyToOne
        @JoinColumn(name = "specialist_id",nullable = false)
        private Specialist specialist;

        @CreationTimestamp
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        public String getFormattedCreatedAt(){
            return createdAt != null ? createdAt.format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "";
        }

        public Request(Consultation consultation,Specialist specialist,RequestPrio prio){
            this.consultation = consultation;
            this.specialist = specialist;
            this.requestPrio = prio;
        }
        public Request(Consultation consultation,Specialist specialist){
            this.consultation = consultation;
            this.specialist = specialist;
        }
    }
