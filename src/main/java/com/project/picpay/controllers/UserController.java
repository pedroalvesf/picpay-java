package com.project.picpay.controllers;

import com.project.picpay.domain.user.User;
import com.project.picpay.dtos.UserDTO;
import com.project.picpay.services.UserServices;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {

  @Autowired
  private UserServices userService;

  @PostMapping
  public ResponseEntity<User> createUser(UserDTO user) {
    User newUser = userService.createUser(user);
    return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }
}
