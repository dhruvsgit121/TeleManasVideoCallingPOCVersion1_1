package com.example.ehrc.telemanas.Configuration;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("configuratioj calling");
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4207, http://172.16.127.6:4209, http://172.16.128.118:8090, http://172.16.188.63 , http://13.232.157.83, http://13.235.128.47, https://telemanas-preprod.iiitb.ac.in/, https://telemanas-test.iiitb.ac.in/, http://35.154.66.77:443  , *") // Example origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "FETCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    //172.16.188.63
    @Bean
    public Filter referrerPolicyFilter() {
        return new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
                chain.doFilter(request, response);
            }
        };
    }
}
