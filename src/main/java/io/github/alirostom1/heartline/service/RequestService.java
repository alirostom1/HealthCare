package io.github.alirostom1.heartline.service;


import io.github.alirostom1.heartline.model.entity.*;
import io.github.alirostom1.heartline.model.enums.RequestPrio;
import io.github.alirostom1.heartline.model.enums.RequestStatus;

import java.util.List;
import java.util.UUID;

public interface RequestService {
     Request createSyncRequest(Consultation consultation, TimeSlot timeSlot, Specialist specialist,String url);
     Request createAsyncRequest(Consultation consultation, Specialist specialist, RequestPrio prio,String question);
     List<Request> getRequestsBySpecialist(Specialist specialist);
     void updateRequestStatus(Request request, RequestStatus status);
     void respondToRequest(UUID requestId, String response);
}
