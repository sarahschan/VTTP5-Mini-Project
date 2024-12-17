package sg.edu.nus.iss.mini_project.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String requestURI = request.getRequestURI();    
    
        Object userID = request.getSession().getAttribute("userID");
        Object userRole = request.getSession().getAttribute("userRole");
    
        if (userID == null) {
            response.sendRedirect("/");
            return false;
        }
    
        if (requestURI.startsWith("/admin") && !userRole.toString().equals("admin")){
            response.sendRedirect("/access-denied");
            return false;
        }
    
        if (requestURI.startsWith("/community") && !userRole.toString().equals("community")) {
            response.sendRedirect("/access-denied");
            return false;
        }
    
        return true;

    }
    
}