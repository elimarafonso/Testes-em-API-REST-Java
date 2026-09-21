package com.techdesk.techdesk.chamados.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.categorias.exception.CategoriaNaoEncontradaException;
import com.techdesk.techdesk.categorias.repository.CategoriaRepository;
import com.techdesk.techdesk.chamados.dto.ChamadoPatchRequestDto;
import com.techdesk.techdesk.chamados.dto.ChamadoRequestDTO;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;
import com.techdesk.techdesk.chamados.entity.Chamado;
import com.techdesk.techdesk.chamados.entity.StatusChamado;
import com.techdesk.techdesk.chamados.exception.ChamadoNaoEncontradoException;
import com.techdesk.techdesk.chamados.exception.StatusChamadoNaoExisteException;
import com.techdesk.techdesk.chamados.repository.ChamadoRepository;


@Service 
public class ChamadoService {

	private final ChamadoRepository chamadoRepository;
	private final CategoriaRepository categoriaRepository;

	public ChamadoService(ChamadoRepository chamadoRepository, CategoriaRepository categoriaRepository) {
		this.chamadoRepository = chamadoRepository;
		this.categoriaRepository = categoriaRepository;
	}

	public ChamadoResponseDTO criar(ChamadoRequestDTO dto) throws Throwable {
		Categoria categoria = (Categoria) categoriaRepository.findById(dto.categoriaId())
				.orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoriaId()));

		Chamado chamado = new Chamado();
		chamado.setTitulo(dto.titulo());
		chamado.setDescricao(dto.descricao());
		chamado.setCategoria(categoria);
		chamado.setStatusChamado(StatusChamado.ABERTO);
		chamado.setDataAbertura(LocalDateTime.now());
		// chamado.setUsuario(usuarioLogado);

		Chamado salvo = chamadoRepository.save(chamado);
		return toResponseDTO(salvo);

	}

	public List<ChamadoResponseDTO> criarEmLote(List<ChamadoRequestDTO> dtos) {

		List<Chamado> chamados = dtos.stream().map(dto -> {

			Chamado chamado = new Chamado();

			chamado.setTitulo(dto.titulo());
			chamado.setDescricao(dto.descricao());
			chamado.setDataAbertura(LocalDateTime.now());
			chamado.setStatusChamado(StatusChamado.ABERTO);

			Categoria categoria = categoriaRepository.findById(dto.categoriaId())
					.orElseThrow(() -> new CategoriaNaoEncontradaException(dto.categoriaId()));

			chamado.setCategoria(categoria);

			return chamado;

		}).toList();

		List<Chamado> saveAll = chamadoRepository.saveAll(chamados);

		return saveAll.stream().map(chamado -> toResponseDTO(chamado)).toList();
	}

	public List<ChamadoResponseDTO> listarTodos() throws Throwable {
		return chamadoRepository.findAll().stream().map(t -> {
			try {
				return toResponseDTO(t);
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				new ChamadoNaoEncontradoException(e.getMessage());
			}
			return null;
		}).toList();
	}

	public ChamadoResponseDTO buscarPorId(Long id) throws Throwable {
		Chamado chamado = chamadoRepository.findById(id)
				.orElseThrow(() -> new ChamadoNaoEncontradoException(id.toString()));

		return toResponseDTO(chamado);
	}

	public ChamadoResponseDTO atualizarStatus(Long id, StatusChamado novoStatus) throws Throwable {
		Chamado chamado = chamadoRepository.findById(id)
				.orElseThrow(() -> new ChamadoNaoEncontradoException(id.toString()));
		chamado.setStatusChamado(novoStatus);
		if (novoStatus == StatusChamado.FECHADO) {
			chamado.setDataFechamento(LocalDateTime.now());
		}
		return toResponseDTO(chamadoRepository.save(chamado));
	}

	public void excluir(Long id) throws Throwable {
		if (!chamadoRepository.existsById(id)) {
			throw new ChamadoNaoEncontradoException(id.toString());
		}
		chamadoRepository.deleteById(id);
	}

	// Método auxiliar privado: converte Entity em DTO — mantém essa conversão
	// num único lugar, em vez de espalhar pelo Controller.

	public ChamadoResponseDTO atualizaChamado(Long id, ChamadoPatchRequestDto chamadoNew) throws Throwable {

		Chamado chamadoOld = chamadoRepository.findById(id)
				.orElseThrow(() -> new ChamadoNaoEncontradoException("chamado nao encontrado"));

		if (chamadoNew.titulo() != null)
			chamadoOld.setTitulo(chamadoNew.titulo());
		if (chamadoNew.descricao() != null)
			chamadoOld.setDescricao(chamadoNew.descricao());
		if (chamadoNew.status() != null) {

			try {
				StatusChamado status = StatusChamado.valueOf(chamadoNew.status());
				chamadoOld.setStatusChamado(status);
			} catch (IllegalArgumentException e) {
				throw new StatusChamadoNaoExisteException(chamadoNew.status());
			}

		}
		if (chamadoNew.categoriaNome() != null) {
			Categoria categoria = categoriaRepository.findByNome(chamadoNew.categoriaNome())
					.orElseThrow(() -> new CategoriaNaoEncontradaException(id));
			chamadoOld.setCategoria(categoria);
		}

		return toResponseDTO(chamadoRepository.save(chamadoOld));
	}

	public static ChamadoResponseDTO toResponseDTO(Chamado c) {
		return new ChamadoResponseDTO(c.getId(), c.getTitulo(), c.getDescricao(), c.getStatusChamado(),
				c.getCategoria().getNome(), c.getDataAbertura());
	}

	public void deletarEmLote(List<Long> ids) {
		chamadoRepository.deleteAllById(ids);
	}

}
