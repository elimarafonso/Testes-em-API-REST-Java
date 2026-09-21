package com.techdesk.techdesk.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techdesk.techdesk.usuarios.dto.LoginRequestDTO;
import com.techdesk.techdesk.usuarios.dto.LoginResponse;
import com.techdesk.techdesk.usuarios.dto.RegisterRequest;
import com.techdesk.techdesk.usuarios.dto.UserResponse;
import com.techdesk.techdesk.usuarios.entity.UserRole;
import com.techdesk.techdesk.usuarios.entity.Usuario;
import com.techdesk.techdesk.usuarios.repository.UsuarioRepository;


@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UsuarioRepository userRepository;
	private final TokenService jwtService;
	private final PasswordEncoder passwordEncoder;

	AuthService(AuthenticationManager authenticationManager, UsuarioRepository userRepository, TokenService jwtService,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
	}

	public LoginResponse login(LoginRequestDTO request) {
		var authToken = new UsernamePasswordAuthenticationToken(request.login(), request.senha());

		// Dispara UserDetailsServiceImpl.loadUserByUsername + validação de senha
		// (BCrypt)
		Authentication authentication = authenticationManager.authenticate(authToken);

		Usuario user = (Usuario) authentication.getPrincipal();
		String token = jwtService.generateToken(user);

		return new LoginResponse(token);
	}

	public UserResponse register(RegisterRequest request) {
		if (userRepository.existsByLogin(request.login())) {
			throw new IllegalArgumentException("Login já cadastrado");
		}

		String encryptedPassword = passwordEncoder.encode(request.senha());
		Usuario newUser = new Usuario(request.login(), encryptedPassword, UserRole.USER);
		userRepository.save(newUser);

		return UserResponse.fromEntity(newUser);
	}
}