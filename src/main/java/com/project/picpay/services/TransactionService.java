package com.project.picpay.services;

import com.project.picpay.domain.transaction.Transaction;
import com.project.picpay.domain.user.User;
import com.project.picpay.dtos.TransactionDTO;
import com.project.picpay.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

  @Autowired
  private UserService userService;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private NotificationService notificationService;

  public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
    User sender = this.userService.findUserById(transactionDTO.senderId());
    User receiver = this.userService.findUserById(transactionDTO.receiverId());

    userService.validateTransaction(sender, transactionDTO.value());

    boolean isAuthorized = this.authorizedTransaction(sender, transactionDTO.value());
    if(!isAuthorized) {
      throw new Exception("Transaction not authorized");
    }

    Transaction newTransaction = new Transaction();
    newTransaction.setAmount(transactionDTO.value());
    newTransaction.setSender(sender);
    newTransaction.setRecipient(receiver);
    newTransaction.setTimestamp(LocalDateTime.now());

    sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
    receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));

    this.userService.saveUser(sender);
    this.userService.saveUser(receiver);
    this.transactionRepository.save(newTransaction);

    this.notificationService.sendNotification(sender, "Transaction completed successfully");
    this.notificationService.sendNotification(receiver, "You received a payment");

    return newTransaction;
  }

  public boolean authorizedTransaction(User sender, BigDecimal value) {
    String url = "http://localhost:8080/authorize";
    ResponseEntity<Map> response = restTemplate.postForEntity(url, sender, Map.class);
    return response.getStatusCode() == HttpStatus.OK;
  }
}
