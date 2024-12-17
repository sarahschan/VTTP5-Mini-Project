package sg.edu.nus.iss.mini_project.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Get userID and userRole from session
        Object userID = request.getSession().getAttribute("userID");
        String userRole = (String) request.getSession().getAttribute("userRole");

        // Allow access to login and error pages without any authentication check
        if (request.getRequestURI().equals("/") || request.getRequestURI().equals("/login") || request.getRequestURI().equals("/error")) {
            return true;
        }

        // Check if userID exists in session
        if (userID == null) {
            response.sendRedirect("/");  // Redirect to home if not logged in
            return false;  // Stop further processing
        }

        // Check if user is trying to access admin pages but is not an admin
        if (request.getRequestURI().startsWith("/admin") && !"admin".equals(userRole)) {
            response.sendRedirect("/access-denied");  // Redirect to access denied page
            return false;  // Stop further processing
        }

        // Check if user is trying to access community pages but is not a community member
        if (request.getRequestURI().startsWith("/community") && !"community".equals(userRole)) {
            response.sendRedirect("/access-denied");  // Redirect to access denied page
            return false;  // Stop further processing
        }

        // Allow the request to continue if role is correct
        return true;
    }
    
}