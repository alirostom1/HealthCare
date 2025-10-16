package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;

import java.time.LocalDate;
import java.util.List;

public interface SpecialistService {
    List<TimeSlot> getTimeSlotsByDate(Specialist specialist, LocalDate date);
    List<Specialist> getAll();
}
