package sg.edu.nus.iss.mini_project.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class Login {
    
        @NotBlank(message = "Please enter your email address")
        @Email(message = "Invalid email address")
    private String email;

        @NotBlank(message = "Please enter your password")
    private String password;


    public Login() {
    }


    @Override
    public String toString() {
        return email + "," + password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
