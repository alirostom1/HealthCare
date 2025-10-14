package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.Generalist;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.enums.ConsultationStatus;
import io.github.alirostom1.heartline.service.ConsultationService;
import io.github.alirostom1.heartline.service.PatientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class GeneralistServlet extends HttpServlet {
    private ConsultationService consultationService;
    private PatientService patientService;
    @Override
    public void init(){
        AppContext appContext = (AppContext) getServletContext().getAttribute("appContext");
        this.consultationService = appContext.getConsultationService();
        this.patientService = appContext.getPatientService();
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
        request.setAttribute("consultations",consultationService.getAllConsultations());
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

}
