package io.github.alirostom1.heartline.filter;

import io.github.alirostom1.heartline.util.CSRFUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class CSRFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String csrfToken = CSRFUtil.getToken(httpRequest);
        httpRequest.setAttribute("csrfToken", csrfToken);

        if (isProtectedMethod(httpRequest.getMethod())) {
            if (!CSRFUtil.isValid(httpRequest)) {
                httpResponse.sendError(403, "Invalid CSRF token");
                return;
            }
        }
        chain.doFilter(request, response);
    }
    private boolean isProtectedMethod(String method) {
        return "POST".equalsIgnoreCase(method) ||
                "PUT".equalsIgnoreCase(method) ||
                "DELETE".equalsIgnoreCase(method);
    }
}
