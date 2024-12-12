package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
            throw new EntityException("Пользователь с таким email уже существует");
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
}
