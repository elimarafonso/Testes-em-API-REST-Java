package com.techdesk.techdesk.chamados.dto;



public record ChamadoPatchRequestDto(

		String titulo, 
		String descricao, 
		String status, 
		String categoriaNome

) {
}
