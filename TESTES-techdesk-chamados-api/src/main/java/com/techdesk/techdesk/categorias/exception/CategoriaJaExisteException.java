package com.techdesk.techdesk.categorias.exception;

public class CategoriaJaExisteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoriaJaExisteException(String nome) {
		super("Já existe uma categoria com o nome: " + nome);
	}

}
