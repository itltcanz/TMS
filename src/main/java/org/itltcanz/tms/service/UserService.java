package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itltcanz.tms.dto.user.UserAuthDto;
import org.itltcanz.tms.dto.user.UserOutDto;
import org.itltcanz.tms.entity.UserEntity;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    // Controllers methods

    public UserOutDto register(UserAuthDto userAuthDto) {
        log.info("Registering user {}", userAuthDto);
        var userEntity = modelMapper.map(userAuthDto, UserEntity.class);

        if (userRepository.existsByEmail(userEntity.getEmail())) {
            throw new EntityException("This email is already in use");
        }

        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setRole(roleService.findUserRole());
        return modelMapper.map(userRepository.save(userEntity), UserOutDto.class);
    }

    public String verify(UserAuthDto userAuthDto) {
        log.info("Verifying user {}", userAuthDto);
        var userEntity = modelMapper.map(userAuthDto, UserEntity.class);
        authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword()));
        return jwtService.generateToken(userEntity.getEmail());
    }

    // Internal methods

    public UserEntity getCurrentUser() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(email);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityException("User not found"));
    }

    public UserEntity findById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityException("User not found"));
    }

    public boolean isAdmin(UserEntity userEntity) {
        return userEntity.getRole().equals(roleService.findAdminRole());
    }
}
