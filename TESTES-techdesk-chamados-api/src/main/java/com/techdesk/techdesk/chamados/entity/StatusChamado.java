package com.techdesk.techdesk.chamados.entity;

// 📁 techdesk-chamados-api/src/main/java/com/techdesk/chamados/entity/StatusChamado.java

// Enum: representa um conjunto FIXO e finito de valores possíveis.
// Mais seguro que usar String solta — o compilador impede um status inválido.
public enum StatusChamado {
    ABERTO, EM_ANDAMENTO, RESOLVIDO, FECHADO
}