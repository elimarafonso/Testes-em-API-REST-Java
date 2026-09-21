package com.techdesk.techdesk.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.techdesk.techdesk.usuarios.repository.UsuarioRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;

    UserDetailsServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        // Chamado automaticamente pelo AuthenticationManager (login) e pelo SecurityFilter (token)
        UserDetails user = repository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + login);
        }
        return user;
    }
}