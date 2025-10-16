package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.TimeSlot;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class TimeSlotRepoImpl implements TimeSlotRepo{
    private final EntityManagerFactory emf;

    public TimeSlotRepoImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public TimeSlot save(TimeSlot ts) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            TimeSlot savedTs;
            if(ts.getId() == null){
                em.persist(ts);
                savedTs = ts;
            }else{
                savedTs = em.merge(ts);
            }
            tx.commit();
            return savedTs;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to save time slot !");
        }finally {
            em.close();
        }
    }
}
