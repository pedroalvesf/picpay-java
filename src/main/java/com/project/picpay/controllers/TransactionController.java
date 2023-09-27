package com.project.picpay.controllers;

import com.project.picpay.domain.transaction.Transaction;
import com.project.picpay.dtos.TransactionDTO;
import com.project.picpay.services.NotificationService;
import com.project.picpay.services.TransactionService;
import com.project.picpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

  @Autowired
  private TransactionService transactionService;
  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
    Transaction newTransaction = this.transactionService.createTransaction(transaction);
    return new ResponseEntity<>(newTransaction, HttpStatus.OK);
  }
}
