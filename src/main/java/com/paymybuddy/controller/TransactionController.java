package com.paymybuddy.controller;

import com.paymybuddy.dto.TransactionDTO;
import com.paymybuddy.dto.UserDTO;
import com.paymybuddy.model.User;
import com.paymybuddy.service.TransactionService;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping("/send")
    public String sendMoney(@ModelAttribute TransactionDTO dto) {
        transactionService.createTransaction(dto);
        return "redirect:/transactions/transfer"; // Redirige vers la page de transfert après l'envoi
    }

    @GetMapping("/transfer")
    public String showTransferPage(Model model) {
        String currentUserEmail = "alice@example.com";// À remplacer plus tard par l'utilisateur connecté

        /*User user = userService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour : " + currentUserEmail));
*/
        UserDTO currentUser = userService.getUserDTO(userService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour : " + currentUserEmail)));

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("buddies", userService.findAllConnections(currentUserEmail));
        model.addAttribute("transactions", transactionService.findAllBySender(currentUserEmail));

        return "transactions"; // Fichier templates/transactions.html
    }

}

