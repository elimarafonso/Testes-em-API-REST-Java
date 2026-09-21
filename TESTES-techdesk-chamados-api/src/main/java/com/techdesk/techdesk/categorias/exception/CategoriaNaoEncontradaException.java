package com.techdesk.techdesk.categorias.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final Long id;
	
	public CategoriaNaoEncontradaException(Long id) {
	        super("Categoria não encontrada com id: "+ id );
	        this.id =id;
	}

	public Long getId() {
		return id;
	}

	

}
