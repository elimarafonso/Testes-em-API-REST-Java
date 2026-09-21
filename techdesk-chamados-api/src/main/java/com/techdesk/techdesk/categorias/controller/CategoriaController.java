package com.techdesk.techdesk.categorias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techdesk.techdesk.categorias.dto.CategoriaPatchRequestDto;
import com.techdesk.techdesk.categorias.dto.CategoriaRequestDto;
import com.techdesk.techdesk.categorias.dto.CategoriaResponseDTO;
import com.techdesk.techdesk.categorias.repository.CategoriaRepository;
import com.techdesk.techdesk.categorias.service.CategoriaService;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("api/categoria")
public class CategoriaController {

	private final CategoriaService service;

	public CategoriaController(CategoriaService service, CategoriaRepository categoriaRepository) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<CategoriaResponseDTO> criar(@RequestBody @Valid CategoriaRequestDto categoria)
			throws Throwable {
		CategoriaResponseDTO cat = service.criar(categoria);
		return ResponseEntity.status(HttpStatus.CREATED).body(cat);
	}

	@GetMapping
	public List<CategoriaResponseDTO> findAll() throws Throwable {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoriaResponseDTO> buscaCategoria(@PathVariable @Positive Long id) throws Exception {
		return ResponseEntity.ok(service.buscar(id));
	}

	@GetMapping("/{idCategoria}/chamados") // todos os chamados por categoria
	public ResponseEntity<List<ChamadoResponseDTO>> buscaChamadosPorCategoria(@PathVariable @Positive Long idCategoria) {
		List<ChamadoResponseDTO> buscarChamadosPorCategoria = service.buscarChamadosPorCategoria(idCategoria);
		return ResponseEntity.ok(buscarChamadosPorCategoria);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletaCategoria(@PathVariable Long id) throws Exception {
		service.excluirCategoria(id);
		return ResponseEntity.noContent().build();// 204 No Content;
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CategoriaResponseDTO> atualizaCategoria(@PathVariable Long id,
			@RequestBody CategoriaPatchRequestDto categoria) {
		CategoriaResponseDTO newCategoria = service.atualiza(id, categoria);
		return ResponseEntity.status(HttpStatus.CREATED).body(newCategoria);
	}

}
