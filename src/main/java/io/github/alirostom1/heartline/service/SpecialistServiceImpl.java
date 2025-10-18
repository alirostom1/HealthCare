package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.Specialty;
import io.github.alirostom1.heartline.repository.SpecialistRepo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpecialistServiceImpl implements SpecialistService{
    private final SpecialistRepo specialistRepo;

    public SpecialistServiceImpl(SpecialistRepo specialistRepo){
        this.specialistRepo = specialistRepo;
    }

    @Override
    public List<TimeSlot> getTimeSlotsByDate(Specialist specialist, LocalDate date) {
        if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            throw new RuntimeException("Date should be a weekday!");
        }
        return specialistRepo.getTimeSlotsByDate(specialist,date);
    }
    @Override
    public List<Specialist> getAll(){
        return specialistRepo.getAll();
    }
    @Override
    public List<Specialist> findBySpecialty(Specialty specialty){
        return specialistRepo.getAll().stream().filter(s -> s.getSpecialty() == specialty).collect(Collectors.toList());
    }

    @Override
    public Optional<Specialist> findById(UUID specialistId) {
        return specialistRepo.findById(specialistId);
    }
    @Override
    public Optional<TimeSlot> getTimeSlotById(UUID timeSlotId) {
        return specialistRepo.getTimeSlotById(timeSlotId);
    }
}
