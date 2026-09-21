package com.techdesk.techdesk.usuarios.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
		@NotNull String login,
		@NotNull  String senha
) {

}
