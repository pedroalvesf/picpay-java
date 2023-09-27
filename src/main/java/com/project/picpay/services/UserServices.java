package com.project.picpay.services;

import com.project.picpay.domain.user.User;
import com.project.picpay.domain.user.UserType;
import com.project.picpay.repository.UserRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

  @Autowired
  private UserRepository userRepository;

  public void validateTransaction(User sender, BigDecimal amount) throws Exception {
    if (sender.getUserType() != UserType.COMMON) {
      throw new Exception("User is not allowed to make transactions");
    }
    if (sender.getBalance().compareTo(amount) < 0) {
      throw new RuntimeException("Insufficient funds");
    }
  }

  public User findUserById(Long id) throws Exception {
    return this.userRepository.findUserById(id)
        .orElseThrow(() -> new Exception("User not found"));
  }

  public User findUserByDocument(String document) throws Exception {
    return this.userRepository.findUserByDocument(document)
        .orElseThrow(() -> new Exception("User not found"));
  }

  public User save(User user) {
    return this.userRepository.save(user);
  }

}

