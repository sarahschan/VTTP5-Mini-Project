package sg.edu.nus.iss.mini_project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController{
    
    @Value("${admin.email}")
    private String adminEmail;

    @Autowired
    ErrorAttributes errorAttributes;

    @GetMapping()
    public String errorPage(WebRequest webRequest, HttpSession session, Model model){

        ErrorAttributeOptions options = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.STATUS);

        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(webRequest, options);

        Integer status = (Integer) errorDetails.get("status");
        
        String message = (String) errorDetails.get("message");

        model.addAttribute("status", status);
        model.addAttribute("message", message);

        Object userID = session.getAttribute("userID");

        if (userID == null){
            return "redirect:/";


        } else if (userID.equals(adminEmail)) {
            return "admin/errorAdmin";


        } else {
            return "community/errorCommunity";
        }

    }
}
