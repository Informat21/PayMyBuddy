package com.paymybuddy.controller;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/add")
    public String addConnection(@RequestParam("email") String buddyEmail,
                                Principal principal,
                                RedirectAttributes redirectAttributes) {

        String userEmail = principal.getName(); // utilisateur connecté
        ConnectionDTO dto = new ConnectionDTO(userEmail, buddyEmail);

        String result = connectionService.addConnection(dto);
        if (result.equals("Connection added")) {
            redirectAttributes.addFlashAttribute("success", "Relation ajoutée avec succès !");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }

        return "redirect:/transactions/transfer"; // Redirige vers la page de transfert
    }

    @GetMapping("/add")
    public String showAddConnectionPage() {
        return "addConnection";
    }
}

