package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Nurse;
import io.github.alirostom1.heartline.model.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepoImpl implements UserRepo{
    private final EntityManagerFactory emf;

    public UserRepoImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public User save(User user) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            User savedUser;
            if(user.getId() != null){
                em.persist(user);
                savedUser = user;
            }else{
                savedUser = em.merge(user);
            }
            tx.commit();
            return savedUser;

        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to create user",e);
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(UUID id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            User user = em.find(User.class,id);
            if(user != null){
                em.remove(user);
            }
            tx.commit();
        }catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to delete user",e);
        }finally{
            em.close();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        try{
            User user = em.find(User.class,id);
            return Optional.ofNullable(user);
        }finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT u from User u",User.class).getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try{
            User user = em.createQuery("SELECT u from User u where u.email=:email",User.class)
                    .setParameter("email",email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(user);
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try{
            User user = em.createQuery("SELECT u from User u where u.username = :username",User.class)
                    .setParameter("username",username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            return Optional.ofNullable(user);
        }finally {
            em.close();
        }
    }

    @Override
    public List<Nurse> findAllNurses() {
        return List.of();
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
