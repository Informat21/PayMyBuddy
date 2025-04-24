package com.paymybuddy.controller;

import com.paymybuddy.dto.LoginDTO;
import com.paymybuddy.dto.UserRegistrationDTO;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthentificationController {

    private final UserService userService;

    // Affiche le formulaire d'inscription
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationDTO dto) {

        return userService.register(dto);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") LoginDTO dto) {
        // logique de connexion ici : vérifier les identifiants
        // ex : return userService.authenticate(dto) ? "redirect:/home" : "login";
        return "redirect:/home";
    }

}
