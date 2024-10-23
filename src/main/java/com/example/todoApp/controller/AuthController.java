package com.example.todoApp.controller;

import com.example.todoApp.dto.JwtResponse;
import com.example.todoApp.dto.UserLoginDTO;
import com.example.todoApp.dto.UserRegistrationDTO;
import com.example.todoApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping("/register")

  public ResponseEntity<JwtResponse> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
    JwtResponse response = userService.registerUser(registrationDTO);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody UserLoginDTO loginDTO) {
    JwtResponse response = userService.authenticateUser(loginDTO);
    return ResponseEntity.ok(response);
  }
}

