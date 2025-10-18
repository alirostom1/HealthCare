package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.model.entity.*;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import io.github.alirostom1.heartline.model.enums.RequestPrio;
import io.github.alirostom1.heartline.model.enums.Specialty;
import io.github.alirostom1.heartline.service.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GeneralistServlet extends HttpServlet {
    private ConsultationService consultationService;
    private PatientService patientService;
    private SpecialistService specialistService;
    private RequestService requestService;

    @Override
    public void init(){
        AppContext appContext = (AppContext) getServletContext().getAttribute("appContext");
        this.consultationService = appContext.getConsultationService();
        this.patientService = appContext.getPatientService();
        this.specialistService = appContext.getSpecialistService();
        this.requestService = appContext.getRequestService();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getPathInfo();
        switch (action){
            case "/":
                showDashboard(request,response);
                break;
            case "/consultation/create":
                createConsultation(request,response);
                break;
            case "/consultations":
                showConsultations(request,response);
                break;
            default:
                if(action.startsWith("/consultation/")){
                    String id = action.substring(14);
                    showConsultation(request,response,id);
                }else{
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                break;
        }
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException,ServletException{
        String action = request.getPathInfo();
        switch (action){
            case "/consultation/create":
                registerConsultation(request,response);
                break;
            case "/consultation/medical-act/add":
                addMedicalAct(request,response);
                break;
            case "/consultation/medical-act/remove":
                removeMedicalAct(request,response);
                break;
            case "/consultation/update-status":
                updateStatus(request,response);
                break;
            case "/consultation/update-diagnosis":
                updateDiagnosis(request,response);
                break;
            case "/consultation/update-treatment":
                updateTreatment(request,response);
                break;
            case "/request/create":
                forwardToRequestForm(request,response);
                break;
            case "/request/sync":
                createSyncRequest(request,response);
                break;
            case "/request/async":
                createAsyncRequest(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void showDashboard(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        notificationMessages(request);
        request.setAttribute("queue",patientService.getQueuePatients());
        request.getRequestDispatcher("/pages/generalist/dashboard.jsp").forward(request,response);
    }


    private void notificationMessages(HttpServletRequest request){
        String success = (String) request.getSession().getAttribute("success");
        String error = (String) request.getSession().getAttribute("error");
        if(success != null) {
            request.setAttribute("success",success);
            request.getSession().removeAttribute("success");
        }
        if(error != null){
            request.setAttribute("error",error);
            request.getSession().removeAttribute("error");
        }
    }
    public void createConsultation(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        notificationMessages(request);
        UUID patientId = UUID.fromString(request.getParameter("patientId"));
        Optional<Patient> optPatient = patientService.findPatientById(patientId);
        if(optPatient.isEmpty()){
            request.setAttribute("error","Patient not found with id : " + patientId);
        }else{
            request.setAttribute("patient",optPatient.get());
        }
        request.getRequestDispatcher("/pages/generalist/consultCreate.jsp").forward(request,response);
    }
    public void registerConsultation(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        UUID patientID = UUID.fromString(request.getParameter("id"));
        Generalist generalist = (Generalist) request.getSession().getAttribute("currentUser");
        String motive = request.getParameter("motive");
        String observations = request.getParameter("observations");
        try{
            Consultation consultation = consultationService.createConsultation(patientID,generalist.getId(),motive,observations);
            request.getSession().setAttribute("success","successfully created consultation for ssn: " + consultation.getPatient().getSsn());
        }catch(Exception e){
            request.getSession().setAttribute("error",e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/generalist/");
    }
    public void showConsultations(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        UUID generalistId = ((Generalist) request.getSession().getAttribute("currentUser")).getId();
        request.setAttribute("consultations",consultationService.getConsultationsGeneralistId(generalistId));
        request.getRequestDispatcher("/pages/generalist/consultations.jsp").forward(request,response);
    }
    public void showConsultation(HttpServletRequest request,HttpServletResponse response,String id)
            throws IOException,ServletException{
        notificationMessages(request);
        UUID consultationId = UUID.fromString(id);
        Optional<Consultation> optConsultation = consultationService.getConsultationById(consultationId);
        if(optConsultation.isEmpty()){
            request.setAttribute("error","Consultation not found with id: " + id);
        }else{
            request.setAttribute("availableMedicalActs",consultationService.getAllAvailableMedicalActs(consultationId));
            request.setAttribute("consultation",optConsultation.get());
            request.setAttribute("specialties", Specialty.values());
        }
        request.getRequestDispatcher("/pages/generalist/consultation.jsp").forward(request,response);
    }

    public void addMedicalAct(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String medicalActId = request.getParameter("medicalActId");
        String consultationId = request.getParameter("consultationId");
        try {
            if(consultationId != null && medicalActId != null &&
                    !consultationId.trim().isEmpty() && !medicalActId.trim().isEmpty()){
                UUID medicalActUuid = UUID.fromString(medicalActId);
                UUID consultationUuid = UUID.fromString(consultationId);
                consultationService.addMedicalAct(consultationUuid,medicalActUuid);
                request.getSession().setAttribute("success","Successfully added medical act!");
                response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
            }else{
                request.getSession().setAttribute("error","Failed to add medical act to consultation!");
                response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
            }
        }catch(Exception e){
            request.getSession().setAttribute("error",e.getMessage());
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }
    }
    public void removeMedicalAct(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String medicalActId = request.getParameter("medicalActId");
        String consultationId = request.getParameter("consultationId");
        try{
            UUID medicalActUuid = UUID.fromString(medicalActId);
            UUID consultationUuid = UUID.fromString(consultationId);
            consultationService.removeMedicalAct(consultationUuid,medicalActUuid);
            request.getSession().setAttribute("success","Successfully removed medical act !");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }catch (Exception e){
            request.getSession().setAttribute("error",e.getMessage());
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }
    }
    public void updateStatus(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String consultationId = request.getParameter("consultationId");
        String  status = request.getParameter("status");
        try{
            UUID consultationUuid = UUID.fromString(consultationId);
            ConsultationStatus consultationStatus = ConsultationStatus.valueOf(status);
            consultationService.updateStatus(consultationUuid,consultationStatus);
            request.getSession().setAttribute("success","Successfully updated status to " + status + " !");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }catch (Exception e){
            request.getSession().setAttribute("error",e.getMessage());
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }
    }
    public void updateDiagnosis(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String consultationId = request.getParameter("consultationId");
        try{
            UUID consultationUuid = UUID.fromString(consultationId);
            consultationService.updateDiagnosis(consultationUuid,request.getParameter("diagnosis"));
            request.getSession().setAttribute("success","Successfully updated consultation diagnosis !");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }catch (Exception e){
            request.getSession().setAttribute("error","Failed to update consultation diagnosis");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);

        }
    }
    public void updateTreatment(HttpServletRequest request,HttpServletResponse response)
        throws IOException,ServletException{
        String consultationId = request.getParameter("consultationId");
        try{
            UUID consultationUuid = UUID.fromString(consultationId);
            consultationService.updateTreatment(consultationUuid,request.getParameter("treatment"));
            request.getSession().setAttribute("success","Successfully updated consultation treatment !");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }catch (Exception e){
            request.getSession().setAttribute("error","Failed to update consultation treatment");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/" + consultationId);
        }
    }
    private void forwardToRequestForm(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        Specialty specialty = Specialty.valueOf(request.getParameter("specialty"));
        List<Specialist> specialists = specialistService.findBySpecialty(specialty);
        request.setAttribute("specialists",specialists);
        if(request.getParameter("type").equals("sync")){
            request.getRequestDispatcher("/pages/generalist/choose-specialist-sync.jsp").forward(request,response);
        }else{
            request.getRequestDispatcher("/pages/generalist/choose-specialist-async.jsp").forward(request,response);
        }
    }
    private void createSyncRequest(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String specialistId = request.getParameter("specialistId");
        String timeSlotId = request.getParameter("timeSlotId");
        String consultationId = request.getParameter("consultationId");
        String url = request.getParameter("meetingUrl");

        Optional<Specialist> optSpecialist = specialistService.findById(UUID.fromString(specialistId));
        if(optSpecialist.isEmpty()){
            request.getSession().setAttribute("error","Invalid specialist Selected");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }
        Specialist specialist = optSpecialist.get();

        Optional<TimeSlot> optTimeSlot = specialistService.getTimeSlotById(UUID.fromString(timeSlotId));
        if(optTimeSlot.isEmpty()){
            request.getSession().setAttribute("error","Invalid timeslot Selected");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }
        TimeSlot timeSlot = optTimeSlot.get();

        Consultation consultation = consultationService.getConsultationById(UUID.fromString(consultationId)).get();
        try{
            requestService.createSyncRequest(consultation,timeSlot,specialist,url);
            request.getSession().setAttribute("success","Successfully placed request!");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }catch (Exception e){
            request.getSession().setAttribute("error","Failed to save Request!");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }
    }
    private void createAsyncRequest(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String specialistId = request.getParameter("specialistId");
        String consultationId = request.getParameter("consultationId");
        String priority = request.getParameter("priority");
        String question = request.getParameter("question");
        Optional<Specialist> optSpecialist = specialistService.findById(UUID.fromString(specialistId));
        if(optSpecialist.isEmpty()){
            request.getSession().setAttribute("error","Invalid specialist Selected");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }
        Specialist specialist = optSpecialist.get();
        Consultation consultation = consultationService.getConsultationById(UUID.fromString(consultationId)).get();
        try{
            requestService.createAsyncRequest(consultation,specialist, RequestPrio.valueOf(priority),question);
            request.getSession().setAttribute("success","Successfully placed request!");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }catch (Exception e){
            e.printStackTrace();
            request.getSession().setAttribute("error","Failed to save Request!");
            response.sendRedirect(request.getContextPath() + "/generalist/consultation/"+consultationId);
        }
    }

}
