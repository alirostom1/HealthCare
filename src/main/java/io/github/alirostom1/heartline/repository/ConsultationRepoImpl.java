package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.MedicalAct;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
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
                if(consultation.getDiagnosis() != null && consultation.getTreatment() != null){
                    consultation.setStatus(ConsultationStatus.COMPLETED);
                }
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
        EntityManager em = emf.createEntityManager();
        try{
            return Optional.ofNullable(em.find(Consultation.class,id));
        }finally {
            em.close();
        }
    }

    @Override
    public List<Consultation> findAll() {
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT c FROM Consultation c ORDER BY c.createdAt DESC",Consultation.class).getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public List<MedicalAct> getAllMedicalActs(){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT ma FROM MedicalAct ma ORDER BY ma.price ASC",MedicalAct.class).getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public Consultation addMedicalAct(UUID consultationId,UUID medicalActId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Consultation consultation = em.find(Consultation.class,consultationId);
            if(consultation == null){
                throw new IllegalArgumentException("Consultation not found with id: " + consultationId);
            }
            MedicalAct medicalAct = em.find(MedicalAct.class,medicalActId);
            if(medicalAct == null){
                throw new IllegalArgumentException("Medical act not found with id : " + medicalActId);
            }
            consultation.getMedicalActs().add(medicalAct);
            em.merge(consultation);
            tx.commit();
            return consultation;
        }catch(Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to add medical Act");
        }finally {
            em.close();
        }
    }
    @Override
    public Consultation removeMedicalAct(UUID consultationId,UUID medicalActId){
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Consultation consultation = em.find(Consultation.class,consultationId);
            if(consultation == null){
                throw new IllegalArgumentException("Consultation not found with id : " + consultationId);
            }
            MedicalAct medicalAct = em.find(MedicalAct.class,medicalActId);
            if(medicalAct == null){
                throw new IllegalArgumentException("Medical act not found with id: " + medicalActId);
            }
            if(!consultation.getMedicalActs().contains(medicalAct)){
                throw new IllegalArgumentException("Medical act isn't attached to consultation!");
            }
            consultation.getMedicalActs().remove(medicalAct);
            em.merge(medicalAct);
            tx.commit();
            return consultation;
        }catch (Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to remove medical act : " + e.getMessage());
        }finally {
            em.close();
        }
    }
}
