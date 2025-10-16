package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.TSstatus;

public interface TimeSlotService{
    TimeSlot create(TimeSlot ts);
    TimeSlot updateStatus(TimeSlot ts, TSstatus status);
}
