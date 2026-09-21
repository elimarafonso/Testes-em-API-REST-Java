package com.techdesk.techdesk.chamados.dto;

import java.time.LocalDateTime;

import com.techdesk.techdesk.chamados.entity.StatusChamado;

public record ChamadoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        StatusChamado status,
        String categoriaNome,
        LocalDateTime dataAbertura
) {}