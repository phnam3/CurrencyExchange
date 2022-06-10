package com.example.authentication.controller;

import com.example.authentication.dto.LoginUserInfo;
import com.example.authentication.dto.RegisterUserInfo;
import com.example.authentication.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserInfo request){
        authService.signUpUser(request);
        return ResponseEntity.ok().body(request);
    }

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@Valid @RequestBody LoginUserInfo request){
        String jwt = authService.verifyLogin(request);
        return ResponseEntity.ok().body(jwt);
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm(){
        log.info("Gateway has routed into confirm api");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
