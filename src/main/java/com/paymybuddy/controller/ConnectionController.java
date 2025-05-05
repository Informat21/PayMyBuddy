package com.paymybuddy.controller;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/add")
    @ResponseBody
    public String addConnection(@RequestBody ConnectionDTO dto) {
        return connectionService.addConnection(dto);
    }

    @GetMapping("/add")
    public String showAddConnectionPage() {
        return "addConnection";
    }
}

