package com.techdesk.techdesk.categorias.exception;

public class CategoriaPossuiChamadosException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public CategoriaPossuiChamadosException(Long id) {
		super("Não é possível Excluir a categoria "+id+
				" porque existem chamados associados a ela.");
	}

}
