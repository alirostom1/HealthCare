package io.github.alirostom1.heartline.filter;

import io.github.alirostom1.heartline.model.entity.User;
import io.github.alirostom1.heartline.model.enums.ERole;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request , ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest  httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        boolean isAuth = session != null && session.getAttribute("currentUser") != null;
        User currentUser = session != null ? (User) session.getAttribute("currentUser") : null;

        // STOP BLOCKING GET /assets/css/output.css or any request towards static content
        if(path.startsWith("/assets/")){
            chain.doFilter(request,response);
            return;
        }
        if(path.equals("/auth/logout")){
            chain.doFilter(request,response);
            return;
        }
        if(path.startsWith("/auth/") || path.equals("/")){
            if(isAuth){
                httpResponse.sendRedirect(httpRequest.getContextPath() + getDashboardPath(currentUser));
                return;
            }
            chain.doFilter(request,response);
            return;
        }
        if(!isAuth){
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
            return;
        }
        if(!hasAccess(currentUser,path)){
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,"Access_denied");
            return;
        }

        chain.doFilter(request,response);
    }
    private String getDashboardPath(User user){
        if(user == null) return "/auth/login";
        switch(user.getRole()){
            case NURSE: return "/nurse/dashboard";
            case GENERALIST: return "/generalist/dashboard";
            default: return "/auth/login";
        }
    }
    private boolean hasAccess(User user,String path){
        if(user == null) return false;
        if(path.startsWith("/nurse/") && user.getRole() == ERole.NURSE) return true;
        if(path.startsWith("/generalist/") && user.getRole() == ERole.GENERALIST) return true;
        return false;
    }

}
