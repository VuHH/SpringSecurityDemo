package com.demo.springsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
  @GetMapping("/public")
  public String hello() {
    return "Hello, public user!";
  }

  @GetMapping("/admin")
  public String admin() {
    return "Hello, admin!";
  }

  @GetMapping("/users")
  public String users() {
    return "Hello, user!";
  }
}
