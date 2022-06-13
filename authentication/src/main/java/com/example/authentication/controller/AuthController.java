package com.example.authentication.controller;

import com.example.authentication.dto.LoginUserInfo;
import com.example.authentication.dto.RegisterUserInfo;
import com.example.authentication.entity.AuthUser;
import com.example.authentication.security.jwt.JwtUtils;
import com.example.authentication.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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
    private final JwtUtils jwtUtils;

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
    public ResponseEntity confirm(@RequestBody String token){
        log.info("Gateway has routed into confirm api");
        String username = jwtUtils.getUserNameFromJwtToken(token);
        String authorities = authService.getAuthorities(username);

        final HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorities", authorities);

        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).build();
    }
}
