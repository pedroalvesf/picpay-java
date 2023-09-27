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
  private UserServices userServices;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private RestTemplate restTemplate;

  public void createTransaction(TransactionDTO transactionDTO) throws Exception {
    User sender = this.userServices.findUserById(transactionDTO.senderId());
    User receiver = this.userServices.findUserById(transactionDTO.receiverId());

    userServices.validateTransaction(sender, transactionDTO.value());

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

    this.userServices.save(sender);
    this.userServices.save(receiver);
    this.transactionRepository.save(newTransaction);
  }

  public boolean authorizedTransaction(User sender, BigDecimal value) {
      ResponseEntity<Map> authorizationResponse = restTemplate
          .getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);

      if(authorizationResponse.getStatusCode() == HttpStatus.OK) {
        String message = (String) authorizationResponse.getBody().get("message");
        return "Autorizado".equalsIgnoreCase(message);
      } else {
        return false;
      }
  }
}
