package com.example.bankapp.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) throws Exception {
        return accountService.getAccount(id);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account account) throws Exception {
        account.setId(id);
        return accountService.updateAccount(account);
    }

    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam double amount
    ) {
        return accountService.transferMoney(fromAccountId, toAccountId, amount);
    }

}