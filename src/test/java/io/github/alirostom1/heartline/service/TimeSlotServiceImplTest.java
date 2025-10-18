package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.enums.TSstatus;
import io.github.alirostom1.heartline.repository.TimeSlotRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeSlotServiceImplTest {

    @Mock
    private TimeSlotRepo timeSlotRepo;

    @Test
    void create_ShouldSaveTimeSlot() {
        TimeSlotService service = new TimeSlotServiceImpl(timeSlotRepo);
        TimeSlot timeSlot = new TimeSlot();
        when(timeSlotRepo.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot result = service.create(timeSlot);

        assertNotNull(result);
        verify(timeSlotRepo).save(timeSlot);
    }

    @Test
    void updateStatus_ShouldUpdateAndSave() {
        TimeSlotService service = new TimeSlotServiceImpl(timeSlotRepo);
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setStatus(TSstatus.AVAILABLE);
        when(timeSlotRepo.save(timeSlot)).thenReturn(timeSlot);

        TimeSlot result = service.updateStatus(timeSlot, TSstatus.UNAVAILABLE);

        assertEquals(TSstatus.UNAVAILABLE, result.getStatus());
        verify(timeSlotRepo).save(timeSlot);
    }
}