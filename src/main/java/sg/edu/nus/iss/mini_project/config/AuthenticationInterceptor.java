package sg.edu.nus.iss.mini_project.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if 'userID' exists in session
        Object userID = request.getSession().getAttribute("userID");

        // If 'userID' is not present, redirect to home page
        if (userID == null) {
            response.sendRedirect("/");
            return false;  // Stop further processing of the request
        }

        // Allow the request to continue
        return true;
    }
}