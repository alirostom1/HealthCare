package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.Specialty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpecialistRepoImpl implements SpecialistRepo{
    private final EntityManagerFactory emf;

    public SpecialistRepoImpl(EntityManagerFactory emf){
        this.emf = emf;
    }
    @Override
    public List<TimeSlot> getTimeSlotsByDate(Specialist specialist, LocalDate date) {
        return specialist.getTimeSlots().stream()
                .filter(timeSlot -> timeSlot.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
    @Override
    public List<Specialist> getAll(){
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s from Specialist s", Specialist.class).getResultList();
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<Specialist> findById(UUID specialistId){
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Specialist.class,specialistId));
        }finally {
            em.close();
        }
    }
    @Override
    public Optional<TimeSlot> getTimeSlotById(UUID timeSlotId){
        EntityManager em = emf.createEntityManager();
        try {
            return Optional.ofNullable(em.find(TimeSlot.class,timeSlotId));
        }finally {
            em.close();
        }
    }
}
