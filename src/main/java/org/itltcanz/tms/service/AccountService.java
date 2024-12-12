package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;

    public Account register(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new EntityException("This email is already in use");
        }
        account.setPassword(encoder.encode(account.getPassword()));
        account.setRole(roleService.findUserRole());
        return accountRepository.save(account);
    }

    public String verify(Account account) {
        authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
        return jwtService.generateToken(account.getEmail());
    }

    public Account getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new EntityException("Account not found"));
    }

    public Account findById(Integer accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new EntityException("Account not found"));
    }


    public boolean isAdmin(Account account) {
        return account.getRole().equals(roleService.findAdminRole());
    }
}
