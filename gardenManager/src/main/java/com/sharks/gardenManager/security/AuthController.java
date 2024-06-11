package com.sharks.gardenManager.security;

import com.sharks.gardenManager.DTO.UserInfoDTO;
import com.sharks.gardenManager.service.UserInfoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserInfoService userInfoService;

    public AuthController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserInfoDTO> login(@Valid @RequestBody LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            request.logout();
            request.login(form.username(), form.password());
        } catch (ServletException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var auth = (Authentication) request.getUserPrincipal();
        var user = (GardenUserDetails) auth.getPrincipal();
        log.info("User {} logged in.", user.getUsername());

        return ResponseEntity.ok(userInfoService.getUserInfo(user.getUsername()));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(Principal principal) {
        if(principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userInfoService.getUserInfo(principal.getName()));
    }

    public record LoginForm(@NotBlank String username, @NotBlank String password) {}
}
