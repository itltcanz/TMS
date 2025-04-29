package org.itltcanz.tms.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.user.UserAuthDto;
import org.itltcanz.tms.dto.user.UserOutDto;
import org.itltcanz.tms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/register")
    @Operation(summary = "Update the priority of a task", description = "This endpoint allows you to update the priority of a task.")
    public ResponseEntity<UserOutDto> register(@RequestBody @Valid UserAuthDto userAuthDto) {
        var userOutDto = userService.register(userAuthDto);
        return ResponseEntity.ok(userOutDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login to the service", description = "This endpoint allows you to login to the service.")
    public ResponseEntity<String> login(@RequestBody @Valid UserAuthDto userAuthDto) {
        var jwtToken = userService.verify(userAuthDto);
        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get user info", description = "This endpoint allows you to get your user information")
    public ResponseEntity<UserOutDto> profile() {
        var currentUser = userService.getCurrentUser();
        var userOutDto = modelMapper.map(currentUser, UserOutDto.class);
        return ResponseEntity.ok(userOutDto);
    }
}
