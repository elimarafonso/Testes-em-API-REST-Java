package com.techdesk.techdesk.usuarios.dto;

public record LoginResponse(
		String token,
		String TokenType) {
	
	public LoginResponse(String token) {
		this(token,"Bearer");
	}

}
