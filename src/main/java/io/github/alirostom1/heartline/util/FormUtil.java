package io.github.alirostom1.heartline.util;

import jakarta.servlet.http.HttpServletRequest;

public class FormUtil {
    public static Integer getIntegerParam(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return (value != null && !value.trim().isEmpty()) ? Integer.parseInt(value.trim()) : null;
    }

    public static Double getDoubleParam(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return (value != null && !value.trim().isEmpty()) ? Double.parseDouble(value.trim()) : 0.0;
    }

    public static String getStringParam(HttpServletRequest request, String param) {
        String value = request.getParameter(param);
        return (value != null && !value.trim().isEmpty()) ? value.trim() : null;
    }
}
