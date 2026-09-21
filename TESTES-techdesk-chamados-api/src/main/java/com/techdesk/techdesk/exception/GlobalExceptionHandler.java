package com.techdesk.techdesk.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.techdesk.techdesk.categorias.exception.CategoriaJaExisteException;
import com.techdesk.techdesk.categorias.exception.CategoriaNaoEncontradaException;
import com.techdesk.techdesk.categorias.exception.CategoriaPossuiChamadosException;
import com.techdesk.techdesk.chamados.exception.ChamadoNaoEncontradoException;
import com.techdesk.techdesk.chamados.exception.StatusChamadoNaoExisteException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponseDTO> handleValidacao(MethodArgumentNotValidException ex) {
		// Junta todos os erros de campo (ex: "titulo: não pode estar vazio") numa única
		// mensagem
		String mensagem = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage()).collect(Collectors.joining(", "));
		ErroResponseDTO erro = new ErroResponseDTO(mensagem, 400, LocalDateTime.now());
		return ResponseEntity.badRequest().body(erro);
	}

	@ExceptionHandler(CategoriaNaoEncontradaException.class)
	public ProblemDetail categoriaNaoEncontrada(CategoriaNaoEncontradaException ex) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

		problem.setTitle("Categoria não encontrada");
		problem.setDetail(ex.getMessage());
		problem.setProperty("categoriaId", ex.getId());

		return problem;
	}

	@ExceptionHandler(ChamadoNaoEncontradoException.class)
	public ProblemDetail chamadoNaoEncontrado(ChamadoNaoEncontradoException ex) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

		problem.setTitle("Chamando NOT FOUND");
		problem.setDetail(ex.getMessage());

		return problem;
	}

	@ExceptionHandler(CategoriaJaExisteException.class)
	public ProblemDetail categoriaJaExisteException(CategoriaJaExisteException ex) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

		problem.setTitle("já existe esta categoria");
		problem.setDetail(ex.getMessage());

		return problem;
	}

	@ExceptionHandler(CategoriaPossuiChamadosException.class)
	public ProblemDetail categoriaPossuiChamadosException(CategoriaPossuiChamadosException ex) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);

		problem.setTitle("Existem chamados para esta categoria.");
		problem.setDetail(ex.getMessage());

		return problem;
	}

	@ExceptionHandler(StatusChamadoNaoExisteException.class)
	public ProblemDetail statusChamadoNaoExisteException(StatusChamadoNaoExisteException ex) {

		ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

		problem.setTitle("Status Invalido.");
		problem.setDetail(ex.getMessage());

		return problem;
	}

	// Captura qualquer outra exceção não prevista — rede de segurança final,
	// evita vazar stack trace interna para quem consome a API.
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponseDTO> handleGenerico(Exception ex) {
		ErroResponseDTO erro = new ErroResponseDTO("Erro interno inesperado.", 500, LocalDateTime.now());
		return ResponseEntity.internalServerError().body(erro);
	}
}