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
        showDashboard(request,response);
    }

    private void loadDashboardData(HttpServletRequest request) {
        request.setAttribute("patients", patientService.getAllPatients());
        request.setAttribute("queue", patientService.getQueuePatients());
        request.setAttribute("todayPatients", patientService.getTodayPatients());
    }
    private void showDashboard(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        loadDashboardData(request);
        request.getRequestDispatcher("/pages/nurse/dashboard.jsp").forward(request,response);

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
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    private void addToQueue(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        try {
            UUID patientId = UUID.fromString(request.getParameter("patientId"));
            patientService.addToQueue(patientId);
            request.setAttribute("success", "Patient added to queue!");
        }catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
        showDashboard(request, response);
    }

    private void addPatient(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        try{
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
            request.setAttribute("succes","Patient Registered Successfully !");
        }catch(Exception e){
            request.setAttribute("error",e.getMessage());
        }
        showDashboard(request,response);
    }

    private void removeFromQueue(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            UUID patientId = UUID.fromString(request.getParameter("patientId"));
            patientService.removeFromQueue(patientId);
            request.setAttribute("success", "Patient removed from queue!");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
        }
        showDashboard(request, response);
    }
    private void showVitalsHistory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Optional<Patient> patient = patientService.findPatientBySsn(request.getParameter("ssn"));
            if (patient.isPresent()) {
                // Get vital signs history
                List<VitalSigns> vitalSignsHistory = patient.get().getVitalSigns();
                request.setAttribute("vitalSignsHistory", vitalSignsHistory);
                request.setAttribute("patient", patient.get());
            }

            request.getRequestDispatcher("/pages/nurse/vitals-history.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", "Error loading vital signs: " + e.getMessage());
            showDashboard(request, response);
        }
    }

}
