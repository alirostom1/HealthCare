package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SpecialistRepo {
    List<TimeSlot> getTimeSlotsByDate(Specialist specialist, LocalDate date);
    List<Specialist> getAll();
}
