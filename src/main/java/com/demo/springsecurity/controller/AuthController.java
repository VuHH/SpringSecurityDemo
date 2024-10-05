package com.demo.springsecurity.controller;

import com.demo.springsecurity.configuration.security.JwtTokenProvider;
import com.demo.springsecurity.entity.User;
import com.demo.springsecurity.repository.UserRepository;
import com.demo.springsecurity.request.LoginRequest;
import com.demo.springsecurity.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private AuthenticationManager authenticationManager;

  private JwtTokenProvider tokenProvider;

  private PasswordEncoder passwordEncoder;

  private UserRepository userRepository;

  @Autowired
  public AuthController(
      AuthenticationManager authenticationManager,
      JwtTokenProvider tokenProvider,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public String authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
    );
    if (authentication.isAuthenticated()) {
      return tokenProvider.generateToken(loginRequest.getUserName());
    } else {
      throw new UsernameNotFoundException("Invalid user request!");
    }
  }

  @PostMapping("/register")
  public String registerUser(@RequestBody RegisterRequest registerRequest) {
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    userRepository.save(user);
    return "User registered successfully";
  }
}
