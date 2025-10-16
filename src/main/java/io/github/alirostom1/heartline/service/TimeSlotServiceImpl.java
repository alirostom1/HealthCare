package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.TSstatus;
import io.github.alirostom1.heartline.repository.TimeSlotRepo;

public class TimeSlotServiceImpl implements TimeSlotService{
    private final TimeSlotRepo timeSlotRepo;

    public TimeSlotServiceImpl(TimeSlotRepo timeSlotRepo){
        this.timeSlotRepo = timeSlotRepo;
    }

    @Override
    public TimeSlot create(TimeSlot ts) {
        return timeSlotRepo.save(ts);
    }

    @Override
    public TimeSlot updateStatus(TimeSlot ts, TSstatus status) {
        ts.setStatus(status);
        return timeSlotRepo.save(ts);
    }
}
