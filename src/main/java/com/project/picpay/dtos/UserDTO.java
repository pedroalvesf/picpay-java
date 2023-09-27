package com.project.picpay.dtos;

import com.project.picpay.domain.user.User;
import com.project.picpay.domain.user.UserType;
import java.math.BigDecimal;

public record UserDTO(
    String firstName, String lastName,
    String document,  String email,
    String password, BigDecimal balance,
    UserType userType) {


  public User createUser(UserDTO user) {
    return new User(user);
  }
}
