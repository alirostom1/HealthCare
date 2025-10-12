package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.exception.PatientAlreadyQueued;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.Queue;
import io.github.alirostom1.heartline.model.entity.VitalSigns;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PatientRepoImpl implements PatientRepo{
    private final EntityManagerFactory emf;

    public PatientRepoImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public Patient save(Patient patient) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Patient savedPatient;
            if(patient.getId() == null){
                em.persist(patient);
                savedPatient = patient;
            }else{
                savedPatient = em.merge(patient);
            }
            tx.commit();
            return savedPatient;
        }catch(Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to save patient",e);
        }finally{
            em.close();
        }
    }

    @Override
    public void delete(UUID id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            Patient patient = em.find(Patient.class,id);
            if(patient != null){
                em.remove(patient);
            }
            tx.commit();
        }catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to delete patient");
        }finally{
            em.close();
        }
    }

    @Override
    public Optional<Patient> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        try{
            return Optional.ofNullable(em.find(Patient.class,id));
        }finally {
            em.close();
        }
    }

    @Override
    public List<Patient> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p from Patient p ORDER BY p.createdAt DESC",Patient.class).getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public Optional<Patient> findBySsn(String ssn) {
        EntityManager em = emf.createEntityManager();
        try {
            Patient patient = em.createQuery("SELECT p FROM Patient p where p.ssn = :ssn",Patient.class)
                    .setParameter("ssn",ssn)
                    .getResultStream()
                    .findFirst().orElse(null);
            return Optional.ofNullable(patient);
        }finally {
            em.close();
        }
    }
    @Override
    public List<Patient> findByFullName(String fullName){
        EntityManager em = emf.createEntityManager();
        try{
            return em.createQuery("SELECT p FROM Patient p " +
                            "where LOWER(p.firstName) Like LOWER(:fullName) OR LOWER(p.lastName) LIKE LOWER(:fullName)" +
                            " ORDER BY p.createdAt DESC",Patient.class)
                    .setParameter("fullName","%" + fullName + "%")
                    .getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public List<Patient> getTodayPatients() {
        EntityManager em = emf.createEntityManager();
        try{
            LocalDate today = LocalDate.now();
            return em.createQuery("SELECT q.patient FROM Queue q WHERE DATE(q.arrivalTime) = :today " +
                    "ORDER BY q.arrivalTime ASC",Patient.class)
                    .setParameter("today",today)
                    .getResultList();
        }finally {
            em.close();
        }
    }

    @Override
    public Patient addVitalSigns(UUID patientId, VitalSigns vitalSigns) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Patient patient = em.find(Patient.class,patientId);
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + patientId);
            }
            em.persist(vitalSigns);
            vitalSigns.setPatient(patient);
            patient.getVitalSigns().add(vitalSigns);
            em.merge(patient);

            tx.commit();
            return patient;
        }catch (Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed to add vital signs",e);
        }finally {
            em.close();
        }
    }


    @Override
    public void addToQueue(UUID patientId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Patient patient = em.find(Patient.class, patientId);
            if (patient == null) {
                throw new IllegalArgumentException("Patient not found with ID: " + patientId);
            }
            Long count = em.createQuery("SELECT COUNT(q) FROM Queue q WHERE q.patient.id = :patientId", Long.class)
                    .setParameter("patientId", patientId)
                    .getSingleResult();
            if (count > 0) {
                throw new PatientAlreadyQueued("Patient " + patient.getFirstName() + " " + patient.getLastName() +
                        " is already in queue !");
            }
            Queue queue = new Queue(patient);
            em.persist(queue);
            tx.commit();
        }catch (PatientAlreadyQueued e){
            if(tx.isActive()) tx.rollback();
            throw e;
        }catch (Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed adding patient to queue",e);
        }finally {
            em.close();
        }
    }

    @Override
    public void removeFromQueue(UUID patientId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            int deletedCount = em.createQuery("DELETE FROM Queue q WHERE q.patient.id = :patientId")
                            .setParameter("patientId",patientId)
                                    .executeUpdate();
            tx.commit();
        }catch (Exception e){
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Failed removing patient from queue ",e);
        }finally {
            em.close();
        }
    }

    @Override
    public List<Patient> getQueuePatients() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT q FROM Queue q ORDER BY q.arrivalTime ASC",Patient.class)
                    .getResultList();
        }finally {
            em.close();
        }
    }
}
