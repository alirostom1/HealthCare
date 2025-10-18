package io.github.alirostom1.heartline.job;

import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.service.SpecialistService;
import io.github.alirostom1.heartline.service.TimeSlotService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

public class GenerateCreatedSpecialistTimeSlots implements Runnable {
    private final TimeSlotService timeSlotService;
    private final Specialist specialist;

    public GenerateCreatedSpecialistTimeSlots(TimeSlotService timeSlotService, Specialist specialist){
        this.timeSlotService = timeSlotService;
        this.specialist = specialist;
    }


    @Override
    public void run() {
        generateCurrentWeekTss(specialist);
        generateNextWeekTss(specialist);
    }

    private void generateNextWeekTss(Specialist specialist){
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        Arrays.stream(DayOfWeek.values()).forEach(day -> {
            LocalDateTime startTime = nextMonday.plusDays(day.ordinal()).atTime(9,0);
            LocalDateTime endTime = nextMonday.plusDays(day.ordinal()).atTime(12,0);
            while(!startTime.equals(endTime)){
                timeSlotService.create(new TimeSlot(specialist,startTime,startTime.plusMinutes(30)));
                startTime = startTime.plusMinutes(30);
            }
        });
    }
    private void generateCurrentWeekTss(Specialist specialist){
        List<TimeSlot> tss = specialist.getTimeSlots();
        LocalDate today = LocalDate.now();
        LocalDate currentWeekMonday = today.with(DayOfWeek.MONDAY);
        Arrays.stream(DayOfWeek.values()).forEach(day -> {
            if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
                return;
            }
            LocalDateTime startTime = currentWeekMonday.plusDays(day.ordinal()).atTime(9,0);
            LocalDateTime endTime = currentWeekMonday.plusDays(day.ordinal()).atTime(12,0);
            while(!startTime.equals(endTime)){
                timeSlotService.create(new TimeSlot(specialist,startTime,startTime.plusMinutes(30)));
                startTime = startTime.plusMinutes(30);
            }
        });

    }

}
