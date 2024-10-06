package com.example.bankapp.transaction;

import com.example.bankapp.account.Account;
import com.example.bankapp.account.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, double amount) throws Exception {
        Account fromAccount = accountService.getAccount(fromAccountId);
        Account toAccount = accountService.getAccount(toAccountId);

        if (fromAccount.getBalance() < amount) {
            throw new Exception("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountService.updateAccount(fromAccount);
        accountService.updateAccount(toAccount);

        transactionRepository.save(new Transaction(fromAccountId, toAccountId, amount));
    }
    
}
