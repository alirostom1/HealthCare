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

public class GenerateWeeklyTimeSlots implements Runnable{
    private final TimeSlotService timeSlotService;
    private final SpecialistService specialistService;

    public GenerateWeeklyTimeSlots(TimeSlotService timeSlotService,SpecialistService specialistService){
        this.timeSlotService = timeSlotService;
        this.specialistService = specialistService;
    }

    @Override
    public void run(){
        specialistService.getAll().stream().forEach(specialist -> {
            if(!hasCurrentWeekTss(specialist)){
                generateCurrentWeekTss(specialist);
            }
            if(!hasNextWeekTss(specialist)){
                generateNextWeekTss(specialist);
            }
        });

    }

    //GENERATE ONLY NEXT WEEK'S TIME SLOTS
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

    //CHECK IF SPECIALIST HAS TIMESLOTS FOR THIS CURRENT WEEK
    private boolean hasCurrentWeekTss(Specialist specialist){
        List<TimeSlot> tss = specialist.getTimeSlots();
        if(tss.isEmpty()){
            return false;
        }
        TimeSlot ts = tss.get(0);
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);

        LocalDateTime weekStart = monday.atStartOfDay();

        return ts.getStartTime().isAfter(weekStart);
    }
    private boolean hasNextWeekTss(Specialist specialist){
        List<TimeSlot> tss = specialist.getTimeSlots();
        if(tss.isEmpty()){
            return false;
        }
        TimeSlot ts = tss.get(0);
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return ts.getStartTime().isAfter(nextMonday.atStartOfDay());
    }

    //GENERATE CURRENT WEEK'S TIMESLOTS
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

