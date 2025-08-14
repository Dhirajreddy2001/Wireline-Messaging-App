package com.chatApplication.wireline.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chatApplication.wireline.model.User;
import com.chatApplication.wireline.repository.UserRepository;


@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean deleteUser(String userId)
    {
        userRepository.deleteById(userId);
        return true;
    }

    public boolean updatePassword(String userId, String oldPassword, String newPassword)
    {
        Optional<User> user_temp = userRepository.findById(userId);

        if(user_temp.isPresent()){
            User user = user_temp.get();

            if(passwordEncoder.matches(oldPassword, user.getPassword()))
            {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;

    }

    public boolean resetPassword(String userId, String newPassword)
    {
        Optional<User> user_temp = userRepository.findById(userId);

        if(user_temp.isPresent())
        {
            User user= user_temp.get();
            user.setPassword(passwordEncoder.encode(newPassword));

          userRepository.save(user);
          return true;
        }

        return false;
    }
    
    
    public Optional<User> getuserByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    
    

}
