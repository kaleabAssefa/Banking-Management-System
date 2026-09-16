package com.bankingapp.banking_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController = @Controller + @ResponseBody combined.
// It tells Spring: "this class handles HTTP requests, and every method's
// return value should be written directly into the HTTP response body
// (as JSON, in our case), not resolved to an HTML view."
@RestController
public class HealthController {

    // @GetMapping maps HTTP GET requests at "/api/health" to this method.
    @GetMapping("/api/health")
    public String healthCheck() {
        return "Banking Management System is up and running!";
    }
}
