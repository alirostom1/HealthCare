package io.github.alirostom1.heartline.repository;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.Specialty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpecialistRepo {
    List<TimeSlot> getTimeSlotsByDate(Specialist specialist, LocalDate date);
    List<Specialist> getAll();
    Optional<Specialist> findById(UUID specialistId);
    Optional<TimeSlot> getTimeSlotById(UUID timeSlotId);
}
