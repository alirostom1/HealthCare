package io.github.alirostom1.heartline.servlet;

import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.model.entity.Specialist;
import io.github.alirostom1.heartline.model.entity.TimeSlot;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.Specialty;
import io.github.alirostom1.heartline.service.SpecialistService;
import io.github.alirostom1.heartline.service.SpecialistServiceImpl;
import io.github.alirostom1.heartline.service.UserService;
import io.github.alirostom1.heartline.util.FormUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

public class SpecialistServlet extends HttpServlet {
    private UserService userService;
    private SpecialistService specialistService;

    @Override
    public void init(){
        AppContext appContext = (AppContext) getServletContext().getAttribute("appContext");
        this.userService = appContext.getUserService();
        this.specialistService = appContext.getSpecialistService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException{
        String action = request.getPathInfo() != null ? request.getPathInfo() : "";
        switch(action){
            case "/":
                showProfile(request,response);
                break;
            case "/time-slots":
                showTimeSlots(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        String action = request.getPathInfo() != null ? request.getPathInfo() : "";
        switch (action){
            case "/update":
                updateSpecialist(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void showProfile(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException{
        notificationMessages(request);
        User user = (User)request.getSession().getAttribute("currentUser");
        Specialist specialist = (Specialist) userService.findById(user.getId()).get();

        request.setAttribute("specialist",specialist);
        request.setAttribute("specialities", Specialty.values());

        request.getRequestDispatcher("/pages/specialist/specialist.jsp").forward(request,response);
    }

    private void updateSpecialist(HttpServletRequest request,HttpServletResponse response)
            throws IOException{
        String specialistId = request.getParameter("specialistId");
        try{
            UUID specialistUuid = UUID.fromString(specialistId);
            Specialty specialty = Specialty.valueOf(request.getParameter("specialty"));
            double fee = FormUtil.getDoubleParam(request,"fee");
            if(fee == 0.00){
                throw new RuntimeException();
            }
            userService.updateSpecialist(specialistUuid,specialty,fee);
            request.getSession().setAttribute("success","Successfully update specialist profile!");
            response.sendRedirect(request.getContextPath() + "/specialist/");
        }catch (Exception e){
            request.getSession().setAttribute("error",e.getMessage());
            response.sendRedirect(request.getContextPath() + "/specialist/");
        }
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
    private void showTimeSlots(HttpServletRequest request,HttpServletResponse response)
            throws IOException,ServletException {
        String inputDate = request.getParameter("date");
        User user = (User) request.getSession().getAttribute("currentUser");
        try {
            Specialist specialist = (Specialist) userService.findById(user.getId()).get();
            LocalDate date = LocalDate.parse(inputDate);
            List<TimeSlot> timeSlots = specialistService.getTimeSlotsByDate(specialist, date);
            request.setAttribute("timeSlots", timeSlots);
            request.getRequestDispatcher("/pages/specialist/time-slots.jsp").forward(request, response);
        }catch(NullPointerException e){
            request.setAttribute("error","Date input must exist!");
            request.getRequestDispatcher("/pages/specialist/time-slots.jsp").forward(request,response);
        }catch(DateTimeParseException e){
            request.setAttribute("error","Invalid date format");
            request.getRequestDispatcher("/pages/specialist/time-slots.jsp").forward(request,response);
        }catch(Exception e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("/pages/specialist/time-slots.jsp").forward(request,response);
        }
    }
}
