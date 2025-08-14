package com.chatApplication.wireline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatApplication.wireline.dto.AuthenticationRequest;
import com.chatApplication.wireline.dto.AuthenticationResponse;
import com.chatApplication.wireline.service.AuthenticationService;



@RestController
@RequestMapping("/Auth")
public class AuthenticationController {

   
    @Autowired
    private AuthenticationService authService;

    public AuthenticationController() {
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {

        AuthenticationResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Session-Id") String sessionId)
    {
        boolean result = authService.logout(sessionId);

        return result ? ResponseEntity.ok("Logout Success")
                                                            : ResponseEntity.status(400).body("Invalid Session");
    }
    

    
    
}
