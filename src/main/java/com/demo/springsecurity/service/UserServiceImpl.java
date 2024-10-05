package com.demo.springsecurity.service;

import com.demo.springsecurity.entity.User;
import com.demo.springsecurity.repository.UserRepository;
import com.demo.springsecurity.request.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {
  private UserRepository repository;

  public UserServiceImpl() {
  }

  @Autowired
  public UserServiceImpl(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    User user = this.repository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Unknown user " + username);
    }
    return new UserDetailsImpl(user);
  }
}
