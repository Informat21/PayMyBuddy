/*package com.paymybuddy.controller;

import com.paymybuddy.model.Connection;
import com.paymybuddy.model.User;
import com.paymybuddy.service.ConnectionService;
import com.paymybuddy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addConnection(@RequestParam String userEmail, @RequestParam String buddyEmail) {
        User user = userService.findByEmail(userEmail).orElse(null);
        User buddy = userService.findByEmail(buddyEmail).orElse(null);

        if (user == null || buddy == null) {
            return ResponseEntity.badRequest().body("User or Buddy not found");
        }

        if (connectionService.areConnected(user, buddy)) {
            return ResponseEntity.badRequest().body("Already connected");
        }

        Connection newConnection = connectionService.addConnection(user, buddy);
        return ResponseEntity.ok(newConnection);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Connection>> listConnections(@RequestParam String userEmail) {
        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(connectionService.getConnectionsForUser(user));
    }
}


 */
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
