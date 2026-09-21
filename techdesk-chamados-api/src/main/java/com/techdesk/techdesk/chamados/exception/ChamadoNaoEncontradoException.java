package com.techdesk.techdesk.chamados.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ChamadoNaoEncontradoException(String string) {
        super("Chamado não encontrada com id: "+ string );
    }
}
