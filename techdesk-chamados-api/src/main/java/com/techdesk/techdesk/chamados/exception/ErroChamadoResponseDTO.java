package com.techdesk.techdesk.chamados.exception;

import java.time.LocalDateTime;

import com.techdesk.techdesk.chamados.entity.StatusChamado;

public record ErroChamadoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusChamado status,
        String categoriaNome,
        LocalDateTime dataAbertura
) {

}
