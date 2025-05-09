package com.paymybuddy.controller;

import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profil")
    public String showProfile(Model model, Principal principal) {
        String email = principal.getName(); // Récupère l'email de l'utilisateur connecté
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "redirect:/auth/login"; // Redirige vers la page de connexion si l'utilisateur n'est pas authentifié
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());

        model.addAttribute("user", userDTO);
        return "profil"; // Correspond à profile.html dans /templates/
    }


    @PostMapping("/profil/update")
    public String updateProfile(@RequestParam String username,
                                @RequestParam String email,
                                @RequestParam(required = false) String password,
                                Principal principal) {

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        user.setUsername(username);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepository.save(user);
        return "redirect:/profil?success";
    }
}
