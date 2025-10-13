package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.model.entity.Consultation;
import io.github.alirostom1.heartline.model.entity.Generalist;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.repository.ConsultationService;
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
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
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
}
