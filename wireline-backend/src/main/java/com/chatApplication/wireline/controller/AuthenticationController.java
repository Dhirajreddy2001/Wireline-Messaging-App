package com.chatApplication.wireline.controller;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatApplication.wireline.dto.AuthenticationRequest;
import com.chatApplication.wireline.dto.AuthenticationResponse;
import com.chatApplication.wireline.service.AuthenticationService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

   
    @Autowired
    private AuthenticationService authService;

    System.Logger logger = System.getLogger(AuthenticationController.class.getName());


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request,
                                                        HttpServletResponse response) {

        AuthenticationResponse authResponse = authService.login(request);
        if(authResponse.getSessionId() != null)
        {
            ResponseCookie cookie = ResponseCookie.from("sessionId", authResponse.getSessionId())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofHours(24))
                .sameSite("Lax")
                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return ResponseEntity.status(400).body("No cookies found");
        }
        String sessionId = Arrays.stream(cookies)
            .filter(c -> c.getName().equals("sessionId"))
            .map(Cookie::getValue)
            .findFirst()
            .orElse(null);

           
        logger.log(System.Logger.Level.INFO, "Session ID: " + sessionId);
        logger.log(System.Logger.Level.DEBUG,"Logout request received");
        boolean result = authService.logout(sessionId);

        return result ? ResponseEntity.ok("Logout Success")
                                                            : ResponseEntity.status(400).body("Invalid Session");
    }
    

    
    
}
