package io.github.alirostom1.heartline.servlet;


import io.github.alirostom1.heartline.config.AppContext;
import io.github.alirostom1.heartline.exception.AuthException;
import io.github.alirostom1.heartline.exception.RegisterException;
import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


public class AuthServlet extends HttpServlet{
    private UserService userService;

    @Override
    public void init() throws ServletException{
        AppContext appContext = (AppContext) getServletContext().getAttribute("appContext");
        this.userService = appContext.getUserService();
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String action = request.getPathInfo();
        switch(action){
            case "/login":
                request.getRequestDispatcher("/pages/auth/login.jsp").forward(request,response);
                break;
            case "/register":
                request.getRequestDispatcher("/pages/auth/register.jsp").forward(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String action = request.getPathInfo();
        switch (action){
            case "/login":
                handleLogin(request,response);
                break;
            case "/register":
                handleRegister(request,response);
                break;
            case "/logout":
                handleLogout(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    private void handleLogin(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try{
            User user = userService.login(username,password);

            HttpSession session = request.getSession();
            session.setAttribute("currentUser",user);

            response.sendRedirect(request.getContextPath() + "/" + user.getRole().toString().toLowerCase() + "/dashboard");

        }catch (AuthException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("/pages/auth/login.jsp").forward(request,response);
        }
    }
    private void handleRegister(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{

        String role = request.getParameter("role").toLowerCase();
        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        try{
            User user;
            switch (role){
                case "nurse":
                    user = userService.registerNurse(fullName,username,email,password);
                    break;
                case "generalist":
                    user = userService.registerGeneralist(fullName,username,email,password);
                    break;
                default:
                    throw new RegisterException("Invalid role");
            }
            HttpSession session = request.getSession();
            session.setAttribute("currentUser",user);

            response.sendRedirect(request.getContextPath() + "/" + role + "/dashboard");
        }catch(RegisterException e){
            request.setAttribute("error",e.getMessage());
            request.getRequestDispatcher("/pages/auth/register.jsp").forward(request,response);
        }
    }
    private void handleLogout(HttpServletRequest request,HttpServletResponse response) throws IOException{
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }


}
