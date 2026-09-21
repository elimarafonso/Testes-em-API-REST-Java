package com.techdesk.techdesk.usuarios.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
		@NotBlank String login, 
		@NotBlank String senha) {

}
