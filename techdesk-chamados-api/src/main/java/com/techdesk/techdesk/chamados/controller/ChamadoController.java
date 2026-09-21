package com.techdesk.techdesk.chamados.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techdesk.techdesk.chamados.dto.ChamadoPatchRequestDto;
import com.techdesk.techdesk.chamados.dto.ChamadoRequestDTO;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;
import com.techdesk.techdesk.chamados.entity.StatusChamado;
import com.techdesk.techdesk.chamados.service.ChamadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chamado")
public class ChamadoController {

	private final ChamadoService service;

	public ChamadoController(ChamadoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<ChamadoResponseDTO> criar(@RequestBody @Valid ChamadoRequestDTO dto) throws Throwable {
	
		ChamadoResponseDTO criado = service.criar(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado); // 201 Created
	}

	@PostMapping("/lote")
	public ResponseEntity<List<ChamadoResponseDTO>> criarEmLote(@RequestBody List<ChamadoRequestDTO> dto) throws Throwable {
		List<ChamadoResponseDTO> criado = service.criarEmLote(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(criado); // 201 Created
	}
	
	
	@GetMapping
	public ResponseEntity<List<ChamadoResponseDTO>> listar() throws Throwable {
		return ResponseEntity.ok(service.listarTodos()); // 200 OK
	}

	@GetMapping("/{id}")
	public ResponseEntity<ChamadoResponseDTO> buscar(@PathVariable Long id) throws Throwable {
		return ResponseEntity.ok(service.buscarPorId(id));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<ChamadoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam StatusChamado status)
			throws Throwable {
		return ResponseEntity.ok(service.atualizarStatus(id, status));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ChamadoResponseDTO> atualizaChamado(@PathVariable Long id,
			@RequestBody ChamadoPatchRequestDto chamadoNew) throws Throwable {

		ChamadoResponseDTO newChamado = service.atualizaChamado(id, chamadoNew);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(newChamado) ;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) throws Throwable {
		service.excluir(id);
		return ResponseEntity.noContent().build(); // 204 No Content
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deletarEmLote(@RequestParam List<Long> ids){
		service.deletarEmLote(ids);
		return ResponseEntity.noContent().build(); // 204 NOCONTENT
	}
	
	
}