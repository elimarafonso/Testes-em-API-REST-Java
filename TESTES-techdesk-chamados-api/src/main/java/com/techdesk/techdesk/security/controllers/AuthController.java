package com.techdesk.techdesk.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techdesk.techdesk.security.service.AuthService;
import com.techdesk.techdesk.usuarios.dto.LoginRequestDTO;
import com.techdesk.techdesk.usuarios.dto.LoginResponse;
import com.techdesk.techdesk.usuarios.dto.RegisterRequest;
import com.techdesk.techdesk.usuarios.dto.UserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequestDTO request) {
		LoginResponse response = authService.login(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
		UserResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
