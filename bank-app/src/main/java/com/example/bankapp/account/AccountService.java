package com.example.bankapp.account;

import com.example.bankapp.transaction.Transaction;
import com.example.bankapp.transaction.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public String transferMoney(Long fromAccountId, Long toAccountId, double amount) {
        // Tìm tài khoản gửi
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Tìm tài khoản nhận
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Kiểm tra xem số dư tài khoản gửi có đủ để chuyển tiền không
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Thực hiện việc trừ tiền từ tài khoản gửi và cộng tiền vào tài khoản nhận
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        // Lưu lại thông tin cập nhật tài khoản
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Lưu giao dịch vào bảng transaction
        Transaction transaction = new Transaction(fromAccountId, toAccountId, amount);
        transactionRepository.save(transaction);

        return "Transfer successful";
    }

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Long id) throws Exception {
        return accountRepository.findById(id)
                .orElseThrow(() -> new Exception("Account not found"));
    }

    public Account updateAccount(Account account) throws Exception {
        if (account.getBalance() < 0) {
            throw new Exception("Balance cannot be negative");
        }
        return accountRepository.save(account);
    }

}

