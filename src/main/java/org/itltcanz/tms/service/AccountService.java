package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.account.AccountAuthDto;
import org.itltcanz.tms.dto.account.AccountOutDto;
import org.itltcanz.tms.entity.Account;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.AccountRepository;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public AccountOutDto register(AccountAuthDto accountAuthDto) {
        var accountEntity = modelMapper.map(accountAuthDto, Account.class);
        if (accountRepository.existsByEmail(accountEntity.getEmail())) {
            throw new EntityException("This email is already in use");
        }
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        accountEntity.setRole(roleService.findUserRole());
        return modelMapper.map(accountRepository.save(accountEntity), AccountOutDto.class);
    }

    public String verify(AccountAuthDto accountAuthDto) {
        var accountEntity = modelMapper.map(accountAuthDto, Account.class);
        authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(accountEntity.getEmail(), accountEntity.getPassword()));
        return jwtService.generateToken(accountEntity.getEmail());
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
