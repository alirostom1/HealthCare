package io.github.alirostom1.heartline.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class NurseServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String action = request.getPathInfo();
        switch (action){
            case "/dashboard":
                showDashboard(request,response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showDashboard(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException{
        request.getRequestDispatcher("/pages/nurse/dashboard.jsp").forward(request,response);
    }
}
