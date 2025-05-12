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
    public String register(@ModelAttribute UserRegistrationDTO dto, Model model) {
        String result = userService.register(dto);

        if ("User registered successfully".equals(result)){
            return "redirect:/auth/login"; // Redirige vers la page de connexion après l'inscription
        } else {
            model.addAttribute("error", result); // Ajoute le message d'erreur au modèle
            return "register"; // Reste sur la page d'inscription en cas d'erreur
        }
        //return userService.register(dto);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("loginDTO") LoginDTO dto) {

        return "redirect:/home";
    }

}
