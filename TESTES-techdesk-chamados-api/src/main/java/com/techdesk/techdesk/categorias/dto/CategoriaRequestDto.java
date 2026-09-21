package com.techdesk.techdesk.categorias.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDto(
		@NotBlank(message = "nome é obrigatório") String nome
){}
