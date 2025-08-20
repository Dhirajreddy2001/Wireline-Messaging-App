package com.chatApplication.wireline.service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatApplication.wireline.dto.AuthenticationRequest;
import com.chatApplication.wireline.dto.AuthenticationResponse;
import com.chatApplication.wireline.model.User;
import com.chatApplication.wireline.repository.UserRepository;



@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest request)
    {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        User user = null;
        

        if( userOpt.isEmpty())
        {
            return new AuthenticationResponse("Invalid Email");
        }

        if(userOpt.isPresent())
        {
            user = userOpt.get();
        }
       
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            return new AuthenticationResponse("Invalid Email or Password", null, null);
        }

        String sessionId = UUID.randomUUID().toString();
        System.Logger logger = System.getLogger(AuthenticationService.class.getName());
        logger.log(System.Logger.Level.INFO, "Session ID: " + sessionId + "\t User name:"+ user.getUsername());
        
        redisTemplate.opsForValue().set(sessionId,user.getId(), 24,TimeUnit.HOURS);

        return new AuthenticationResponse("Login Success", user.getId(), sessionId);
        
    }

    public boolean logout(String sessionId)
    {
        if(sessionId == null || sessionId.isEmpty())
        {
            throw new IllegalArgumentException("Session ID cannot be null or empty");
        }
        return Boolean.TRUE.equals(redisTemplate.delete(sessionId));
    }
}
