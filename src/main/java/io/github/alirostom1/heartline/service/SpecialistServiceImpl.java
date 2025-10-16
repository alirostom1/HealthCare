package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.repository.SpecialistRepo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

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
}
