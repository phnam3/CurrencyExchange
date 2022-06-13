package com.example.authentication.service;

import com.example.authentication.dto.LoginUserInfo;
import com.example.authentication.dto.RegisterUserInfo;
import com.example.authentication.entity.AuthUser;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    public void signUpUser(RegisterUserInfo request){
        if(userRepository.findByEmail(request.getEmail()).isPresent())
            throw new IllegalArgumentException("User Found: Email Existed");
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        AuthUser authUser = new AuthUser(
                request.getEmail(),
                encodedPassword,
                request.getRoles()
        );
        userRepository.save(authUser);
    }

    public String verifyLogin(LoginUserInfo request) {
        AuthUser authUser = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Email not existed")
        );
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(request.getEmail());
    }

    public String getAuthorities(String email){
        AuthUser authUser = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Email not existed")
        );
        return authUser.getRole();
    }
}
