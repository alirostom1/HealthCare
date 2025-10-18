package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class ResponseRepositoryImpl implements ResponseRepository{
    private final EntityManagerFactory emf;

    public ResponseRepositoryImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Response save(Response response) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Response savedResponse;
            if(response.getId() == null){
                em.persist(response);
                savedResponse = response;
            }else{
             savedResponse = em.merge(response);
            }
            tx.commit();
            return savedResponse;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to save response");
        }finally {
            em.close();
        }
    }
}
