package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.model.entity.Patient;
import io.github.alirostom1.heartline.model.entity.VitalSigns;
import io.github.alirostom1.heartline.service.PatientService;
import io.github.alirostom1.heartline.util.FormUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.time.LocalDate;

import java.util.Optional;
import java.util.UUID;

public class NurseServlet extends HttpServlet {
    private PatientService patientService;

    @Override
    public void init(){
        AppContext appContext = (AppContext) getServletContext().getAttribute("appContext");
        this.patientService = appContext.getPatientService();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getPathInfo();
        switch (action){
            case "/":
                showDashboard(request,response);
                break;
            case "/queue":
                showQueue(request,response);
                break;
            default:
                if (action.startsWith("/search/")) {
                    String ssn = action.substring(8); // Remove "/search/" prefix
                    searchPatient(request, response, ssn);
                }
                break;
        }
    }


    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        String action = request.getPathInfo();
        switch(action){
            case "/queue/add":
                addToQueue(request,response);
                break;
            case "/patient/register":
                addPatient(request,response);
                break;
            case "/queue/remove":
                removeFromQueue(request,response);
                break;
            case "/patient/vitals/add":
                addVitals(request,response);
                break;
            case "/patient/update":
                updatePatient(request,response);
                break;
            case "/search":
                String ssn = request.getParameter("ssn");
                searchPatient(request,response,ssn);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    private void showDashboard(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        notificationMessages(request);
        request.setAttribute("patients",patientService.getAllPatients());
        request.getRequestDispatcher("/pages/nurse/dashboard.jsp").forward(request,response);
    }
    private void showQueue(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        notificationMessages(request);
        request.setAttribute("queue",patientService.getQueuePatients());
        request.getRequestDispatcher("/pages/nurse/queue.jsp").forward(request,response);
    }

    private void addToQueue(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        try {
            UUID patientId = UUID.fromString(request.getParameter("id"));
            patientService.addToQueue(patientId);
            request.getSession().setAttribute("success","Patient added to queue!");
        }catch (Exception e) {
            request.getSession().setAttribute("error",e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/nurse/");
    }

    private void addPatient(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        try{
            String ssn = request.getParameter("ssn");

            Optional<Patient> existingPatient = patientService.findPatientBySsn(ssn);
            if (existingPatient.isPresent()) {
                request.getSession().setAttribute("error", "SSN already exists for another patient!");
                response.sendRedirect(request.getContextPath() + "/nurse/");
                return;
            }
            Patient patient = new Patient(
                    request.getParameter("firstName"),
                    request.getParameter("lastName"),
                    LocalDate.parse(request.getParameter("birthDate")),
                    request.getParameter("ssn"),
                    request.getParameter("phoneNumber")
            );
            VitalSigns vs = null;
            String bp = request.getParameter("bloodPressure");
            if(bp != null && !bp.trim().isEmpty()){
                vs = new VitalSigns(
                        null,
                        bp,
                        FormUtil.getIntegerParam(request, "heartRate"),
                        FormUtil.getDoubleParam(request, "bodyTemperature"),
                        FormUtil.getIntegerParam(request, "respiratoryRate"),
                        FormUtil.getDoubleParam(request, "weight"),
                        FormUtil.getDoubleParam(request, "height")
                );
            }
            patientService.registerPatient(patient,vs);
            request.getSession().setAttribute("succes","Patient Registered Successfully !");
            response.sendRedirect(request.getContextPath() + "/nurse/");
        }catch(Exception e){
            request.getSession().setAttribute("error",e.getMessage());
            response.sendRedirect(request.getContextPath() + "/nurse/");
        }
    }

    private void removeFromQueue(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            UUID patientId = UUID.fromString(request.getParameter("patientId"));
            patientService.removeFromQueue(patientId);
            request.getSession().setAttribute("success", "Patient removed from queue!");
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/nurse/queue");
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
    private void searchPatient(HttpServletRequest request,HttpServletResponse response,String ssn)
            throws IOException,ServletException{
        notificationMessages(request);
        Optional<Patient> patient = patientService.findPatientBySsn(ssn);
        if(patient.isPresent()){
            request.setAttribute("patient",patient.get());
        }else{
            request.setAttribute("error","Patient with ssn: " + ssn + " not found");
        }
        request.getRequestDispatcher("/pages/nurse/patient.jsp").forward(request,response);
    }
    private void addVitals(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        try{
            String id = request.getParameter("id");
            String ssn = request.getParameter("ssn");

            Optional<Patient> optPatient =  patientService.findPatientBySsn(ssn);
            if(optPatient.isPresent()){
                Patient patient = optPatient.get();
                VitalSigns vs = new VitalSigns(
                        patient,
                        FormUtil.getStringParam(request,"bloodPressure"),
                        FormUtil.getIntegerParam(request, "heartRate"),
                        FormUtil.getDoubleParam(request, "bodyTemperature"),
                        FormUtil.getIntegerParam(request, "respiratoryRate"),
                        FormUtil.getDoubleParam(request, "weight"),
                        FormUtil.getDoubleParam(request, "height")
                );
                patientService.addVitalSigns(UUID.fromString(id),vs);
                request.getSession().setAttribute("succes","Vital signs added successfully !");
            }else{
                request.getSession().setAttribute("error","Patient not found");
            }

        }catch (Exception e){
            request.getSession().setAttribute("error","Error add vital signs record: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/nurse/search/" + request.getParameter("ssn"));
    }
    public void updatePatient(HttpServletRequest request, HttpServletResponse response)
            throws IOException,ServletException{
        try {
            UUID patientId = UUID.fromString(request.getParameter("patientId"));
            Optional<Patient> optPatient = patientService.findPatientById(patientId);
            if(optPatient.isPresent()){
                Patient patient = optPatient.get();
                String newSsn = request.getParameter("ssn");
                if(!patient.getSsn().equals(newSsn)){
                    Optional<Patient> patientWithSsn = patientService.findPatientBySsn(newSsn);
                    if(patientWithSsn.isPresent()){
                        request.getSession().setAttribute("error","SSN already exists for another patient!");
                        response.sendRedirect(request.getContextPath()+ "/nurse/search/" + patient.getSsn());
                        return;
                    }
                }
                patient.setFirstName(request.getParameter("firstName"));
                patient.setLastName(request.getParameter("lastName"));
                patient.setBirthDate(LocalDate.parse(request.getParameter("birthDate")));
                patient.setSsn(newSsn);
                patient.setPhoneNumber(request.getParameter("phoneNumber"));

                patientService.updatePatient(patient);
                request.getSession().setAttribute("success", "Patient updated Successfully");
                response.sendRedirect(request.getContextPath() + "/nurse/search/" + newSsn);
            }else{
                request.getSession().setAttribute("error","Patient not found!");
                response.sendRedirect(request.getContextPath() + "/nurse/");
            }
        }catch (Exception e){
            request.getSession().setAttribute("error","Error updating patient: " +  e.getMessage());
            response.sendRedirect(request.getContextPath() + "/nurse/");
        }

    }


}
