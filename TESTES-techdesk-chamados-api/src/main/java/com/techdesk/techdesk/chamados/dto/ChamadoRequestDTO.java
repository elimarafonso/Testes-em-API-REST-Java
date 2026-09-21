package com.techdesk.techdesk.chamados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


//record: forma enxuta do Java para uma classe imutável, ideal para DTOs.
//As anotações de validação (@NotBlank, @NotNull) são checadas automaticamente
//pelo Spring quando o DTO chega no Controller com @Valid.
public record ChamadoRequestDTO(
     @NotBlank String titulo,
     @NotBlank String descricao,
     @NotNull Long categoriaId
) {}