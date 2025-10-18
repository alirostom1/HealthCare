package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RequestRepositoryImpl implements RequestRepository{
    private final EntityManagerFactory emf;

    public RequestRepositoryImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Request save(Request r) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Request savedRequest;
            if(r.getId() == null){
                em.persist(r);
                savedRequest = r;
            }else{
                savedRequest = em.merge(r);
            }
            tx.commit();
            return savedRequest;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Failed saving request!");
        }finally {
            em.close();
        }
    }

    @Override
    public List<Request> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r from Request r",Request.class).getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<Request> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        try{
            Request r = em.find(Request.class,id);
            return Optional.ofNullable(r);
        }finally {
            em.close();
        }
    }
}
