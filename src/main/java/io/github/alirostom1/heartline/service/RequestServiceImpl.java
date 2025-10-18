package io.github.alirostom1.heartline.service;

import io.github.alirostom1.heartline.model.entity.*;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import io.github.alirostom1.heartline.model.enums.RequestPrio;
import io.github.alirostom1.heartline.model.enums.RequestStatus;
import io.github.alirostom1.heartline.model.enums.TSstatus;
import io.github.alirostom1.heartline.repository.ConsultationRepo;
import io.github.alirostom1.heartline.repository.RequestRepository;
import io.github.alirostom1.heartline.repository.ResponseRepository;
import io.github.alirostom1.heartline.repository.TimeSlotRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestServiceImpl implements RequestService{
    private final RequestRepository requestRepository;
    private final TimeSlotRepo timeSlotRepo;
    private final ConsultationRepo consultationRepo;
    private final ResponseRepository responseRepository;

    public RequestServiceImpl(RequestRepository requestRepository,TimeSlotRepo timeSlotRepo,ConsultationRepo consultationRepo, ResponseRepository responseRepository){
        this.requestRepository = requestRepository;
        this.timeSlotRepo = timeSlotRepo;
        this.consultationRepo = consultationRepo;
        this.responseRepository = responseRepository;
    }
    @Override
    public Request createSyncRequest(Consultation consultation, TimeSlot timeSlot, Specialist specialist,String url) {
        timeSlot.setStatus(TSstatus.UNAVAILABLE);
        timeSlotRepo.save(timeSlot);
        consultation.setStatus(ConsultationStatus.AWAITING_SPECIALIST_REVIEW);
        consultationRepo.save(consultation);
        Request request = new SyncRequest(consultation, specialist, timeSlot, url);
        return requestRepository.save(request);
    }
    @Override
    public Request createAsyncRequest(Consultation consultation, Specialist specialist, RequestPrio prio,String question){
        consultation.setStatus(ConsultationStatus.AWAITING_SPECIALIST_REVIEW);
        consultationRepo.save(consultation);
        Request request = new AsyncRequest(consultation,specialist,prio,question);
        return requestRepository.save(request);
    }

    @Override
    public List<Request> getRequestsBySpecialist(Specialist specialist) {
        return requestRepository.findAll().stream()
                .filter(r ->
                        r.getSpecialist().getId().equals(specialist.getId())
                        && r.getStatus().equals(RequestStatus.WAITING)
                        && (!(r instanceof SyncRequest) || !((SyncRequest) r).getTimeSlot().isPast())
                )
                .collect(Collectors.toList());
    }

    @Override
    public void updateRequestStatus(Request request, RequestStatus status) {
        request.setStatus(status);
        requestRepository.save(request);
    }

    @Override
    public void respondToRequest(UUID requestId, String response) {
        Optional<Request> request = requestRepository.findById(requestId);
        if(request.isEmpty()){
            throw new RuntimeException("Request with such id not found: " + requestId);
        }
        AsyncRequest asyncRequest = (AsyncRequest) request.get();
        Response resp = new Response(asyncRequest,response);
        responseRepository.save(resp);
        asyncRequest.setStatus(RequestStatus.COMPLETED);
        requestRepository.save(asyncRequest);
    }

}
