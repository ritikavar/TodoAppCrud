package com.example.todoApp.service;

import com.example.todoApp.dto.*;
import com.example.todoApp.exception.CustomException;
import com.example.todoApp.model.User;
import com.example.todoApp.repository.UserRepository;
import com.example.todoApp.security.JwtUtil;
import com.example.todoApp.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtUtil jwtUtil,
                     UserDetailsServiceImpl userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

  public JwtResponse registerUser(UserRegistrationDTO registrationDTO) {
    if (userRepository.existsByEmail(registrationDTO.getEmail())) {
      throw new CustomException("Email is already in use");
    }
    User user = new User();
    user.setName(registrationDTO.getName());
    user.setEmail(registrationDTO.getEmail());
    user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
    userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
    String token = jwtUtil.generateToken(userDetails);

    return new JwtResponse(token);
  }

  public JwtResponse authenticateUser(UserLoginDTO loginDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
    } catch (BadCredentialsException e) {
      throw new CustomException("Invalid email or password");
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
    String token = jwtUtil.generateToken(userDetails);

    return new JwtResponse(token);
  }
}
