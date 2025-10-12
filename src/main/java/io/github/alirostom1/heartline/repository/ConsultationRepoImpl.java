package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Consultation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ConsultationRepoImpl implements ConsultationRepo{
    private final EntityManagerFactory emf;

    public ConsultationRepoImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Consultation save(Consultation consultation) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Consultation savedConsult;
            if(consultation.getId() == null){
                em.persist(consultation);
                savedConsult = consultation;
            }else{
                savedConsult = em.merge(consultation);
            }
            tx.commit();
            return savedConsult;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to save consultation");
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public Optional<Consultation> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Consultation> findAll() {
        return List.of();
    }
}
