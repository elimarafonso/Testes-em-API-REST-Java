package com.techdesk.techdesk.chamados.exception;

public class StatusChamadoNaoExisteException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public StatusChamadoNaoExisteException(String status) {
		super("Status inválido: "+status);
	}

}
