package sg.edu.nus.iss.mini_project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    
    @Autowired
    AuthenticationInterceptor authenticationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add the interceptor to the specified paths
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/admin/**", "/community/**")  // Apply to these paths
                .excludePathPatterns("/", "/login", "/error", "/access-denied");  // Exclude login, error, and access-denied pages
    }

}
