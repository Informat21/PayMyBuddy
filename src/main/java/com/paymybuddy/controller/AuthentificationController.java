package com.paymybuddy.controller;

import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthentificationController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationDTO dto) {
        return userService.register(dto);
    }
}
