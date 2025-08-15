package com.chatApplication.wireline.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatApplication.wireline.dto.PasswordUpdateRequest;
import com.chatApplication.wireline.model.User;
import com.chatApplication.wireline.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user)
    {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok("New User Created");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id)
    {
        boolean deleteUser = userService.deleteUser(id);
        if(deleteUser)
        {
            return ResponseEntity.ok("User Deleted Successfully");
        }
        else{
            return ResponseEntity.status(400).body("User not found or cannot be deleted");
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable String id, @RequestBody PasswordUpdateRequest request)
    {
        boolean update = userService.updatePassword(id, request.getOld_password(), request.getNew_password());

        if(update){
            return ResponseEntity.status(200).body("Password Updated Successfully");
        }
        else{
            return ResponseEntity.status(400).body("Incorrent Current password or user not found");
        }
    }

    @PutMapping("/{id}/reset_password")
    public ResponseEntity<String> resetPassword(@PathVariable String id, @RequestBody PasswordUpdateRequest request)
    {
        boolean reset_password = userService.resetPassword(id, request.getNew_password());

        if(reset_password)
        {
            return ResponseEntity.status(200).body("Password Update Success");
        }
        else{
            return ResponseEntity.status(400).body("user not found");
        }
    }

    @GetMapping("/by_username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username)
    {
        Optional<User> user =  userService.getuserByUsername(username);
        return user.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
