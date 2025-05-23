package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final ConnectionService connectionService;

    @PostMapping("/send")
    public String sendMoney(@ModelAttribute TransactionDTO dto) {
        try {
            transactionService.createTransaction(dto);
        return "redirect:/transactions/transfer"; // Redirige vers la page de transfert après l'envoi
    } catch (IllegalArgumentException e) {
            return "redirect:/transactions/transfer?error=" + e.getMessage();
        }
    }

    @GetMapping("/transfer")
    public String showTransferPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login"; // Redirige vers la page de connexion si l'utilisateur n'est pas authentifié
        }
        String currentUserEmail = principal.getName();// Récupère l'email


        // 1. Transactions envoyées
        List<TransactionDTO> transactions = transactionService.findAllBySender(currentUserEmail);

        // 2. Buddies (connexions)

        UserDTO currentUser = userService.getUserDTO(userService.findByEmail(currentUserEmail)
               .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour : " + currentUserEmail)));

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("buddies", userService.findAllConnections(currentUserEmail));
        model.addAttribute("transactions", transactions);

        return "transactions"; // Fichier templates/transactions.html
    }

}

