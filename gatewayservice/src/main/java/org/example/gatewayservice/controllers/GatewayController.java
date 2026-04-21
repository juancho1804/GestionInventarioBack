package org.example.gatewayservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @GetMapping("/wakeup")
    public ResponseEntity<String>wakeUp(){
        return ResponseEntity.ok("Gateway working...");
    }
}
