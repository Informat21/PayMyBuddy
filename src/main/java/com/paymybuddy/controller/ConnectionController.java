package com.paymybuddy.controller;

import com.paymybuddy.dto.ConnectionDTO;
import com.paymybuddy.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;

    @PostMapping("/add")
    public String addConnection(@RequestBody ConnectionDTO dto) {
        return connectionService.addConnection(dto);
    }
}
