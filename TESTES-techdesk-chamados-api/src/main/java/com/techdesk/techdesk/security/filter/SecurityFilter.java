package com.techdesk.techdesk.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techdesk.techdesk.security.service.TokenService;
import com.techdesk.techdesk.usuarios.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService jwtService;
    private final UsuarioRepository userRepository;

    SecurityFilter(UsuarioRepository userRepository, TokenService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            String login = jwtService.validateToken(token);
            if (login != null) {
                UserDetails user = userRepository.findByLogin(login);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                if (token != null) 
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}