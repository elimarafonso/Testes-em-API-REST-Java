package com.techdesk.techdesk.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.techdesk.techdesk.usuarios.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
  
	// Usado pelo UserDetailsServiceImpl e também para checar duplicidade no registro
    UserDetails findByLogin(String login);

    boolean existsByLogin(String login);
}
