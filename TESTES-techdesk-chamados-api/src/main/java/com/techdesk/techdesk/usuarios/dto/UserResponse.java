package com.techdesk.techdesk.usuarios.dto;

import com.techdesk.techdesk.usuarios.entity.UserRole;
import com.techdesk.techdesk.usuarios.entity.Usuario;

public record UserResponse(
		Long id,
		String login,
		UserRole role) {
	
	
	public static UserResponse fromEntity(Usuario user) {
		return new UserResponse(user.getId(), user.getLogin(), user.getRole());
	}
}