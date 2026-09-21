package com.techdesk.techdesk.categorias.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.techdesk.techdesk.TechdeskChamadosApiApplication;
import com.techdesk.techdesk.categorias.dto.CategoriaPatchRequestDto;
import com.techdesk.techdesk.categorias.dto.CategoriaRequestDto;
import com.techdesk.techdesk.categorias.dto.CategoriaResponseDTO;
import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.categorias.exception.CategoriaJaExisteException;
import com.techdesk.techdesk.categorias.exception.CategoriaNaoEncontradaException;
import com.techdesk.techdesk.categorias.exception.CategoriaPossuiChamadosException;
import com.techdesk.techdesk.categorias.repository.CategoriaRepository;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;
import com.techdesk.techdesk.chamados.entity.Chamado;
import com.techdesk.techdesk.chamados.repository.ChamadoRepository;
import com.techdesk.techdesk.chamados.service.ChamadoService;

@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;
	private final ChamadoRepository chamadoRepository;

	public CategoriaService(CategoriaRepository categoriaRepository,
			TechdeskChamadosApiApplication techdeskChamadosApiApplication, ChamadoRepository chamadoRepository) {
		this.categoriaRepository = categoriaRepository;
		this.chamadoRepository = chamadoRepository;

	}

	// TESTE CRIADO
	public CategoriaResponseDTO criar(CategoriaRequestDto categoriaDto) throws Throwable {

		categoriaRepository.findByNome(categoriaDto.nome()).ifPresent(categoria -> {
			throw new CategoriaJaExisteException(categoriaDto.nome());
		});

		Categoria cat = new Categoria();
		cat.setNome(categoriaDto.nome());

		return toCategoryDto(categoriaRepository.save(cat));

	}

	// TESTE CRIADO
	public List<CategoriaResponseDTO> findAll() {
		return categoriaRepository.findAll().stream().map(this::toCategoryDto).toList();
	}
    //TESTE CRIADO
	public List<ChamadoResponseDTO> buscarChamadosPorCategoria(Long idCategoria) {

		Categoria categoria = categoriaRepository.findById(idCategoria)
				.orElseThrow(() -> new CategoriaNaoEncontradaException(idCategoria));

		List<Chamado> chamadosPorCategoria = chamadoRepository.findByCategoria(categoria);

		List<ChamadoResponseDTO> list = chamadosPorCategoria.stream()
				.map(chamados -> ChamadoService.toResponseDTO(chamados)).toList();

		return list;
	}

	// TESTE CRIADO
	public CategoriaResponseDTO buscar(Long id) {
		Categoria cat = categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNaoEncontradaException(id));
		return toCategoryDto(cat);
	}

	
	//##################
	//##################
	public void excluirCategoria(Long id) {
		if (!categoriaRepository.existsById(id)) {
			throw new CategoriaNaoEncontradaException(id);
		} else if (chamadoRepository.existsByCategoriaId(id)) {
			throw new CategoriaPossuiChamadosException(id);
		}
		categoriaRepository.deleteById(id);
	}
	//##################
	//##################

	public CategoriaResponseDTO atualiza(Long id, CategoriaPatchRequestDto categoriaNew) {
		Categoria categoriaOld = categoriaRepository.findById(id)
				.orElseThrow(() -> new CategoriaNaoEncontradaException(id));
		if (categoriaNew.nome() != null)
			categoriaOld.setNome(categoriaNew.nome());
		categoriaRepository.save(categoriaOld);
		return toCategoryDto(categoriaOld);
	}

	private CategoriaResponseDTO toCategoryDto(Categoria toDto) {
		return new CategoriaResponseDTO(toDto.getId(), toDto.getNome());
	}

}
