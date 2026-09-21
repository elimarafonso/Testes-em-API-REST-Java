package com.techdesk.techdesk.categorias.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techdesk.techdesk.categorias.dto.CategoriaRequestDto;
import com.techdesk.techdesk.categorias.dto.CategoriaResponseDTO;
import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.categorias.exception.CategoriaJaExisteException;
import com.techdesk.techdesk.categorias.exception.CategoriaNaoEncontradaException;
import com.techdesk.techdesk.categorias.repository.CategoriaRepository;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;
import com.techdesk.techdesk.chamados.entity.Chamado;
import com.techdesk.techdesk.chamados.repository.ChamadoRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

	@InjectMocks
	private CategoriaService categoriaService;

	@Mock
	private ChamadoRepository chamadoRepository;

	@Mock
	private CategoriaRepository categoriaRepository;

	@Test
	@DisplayName("Deve Retornar uma Lista de Categorias")
	public void deveRetornarUmaListaDeCategorias() {

		Categoria categoriaManutencao = new Categoria(1L, "Manutencao", null);
		Categoria categoriaRecepcao = new Categoria(2L, "Recepcao", null);

		List<Categoria> categoriaList = new ArrayList<>();
		categoriaList.add(categoriaManutencao);
		categoriaList.add(categoriaRecepcao);

		Mockito.when(categoriaRepository.findAll()).thenReturn(categoriaList);

		List<CategoriaResponseDTO> allCategory = categoriaService.findAll();

		assertNotNull(allCategory);
		assertEquals(2, allCategory.size());

		assertEquals(1L, allCategory.get(0).id());
		assertEquals("Manutencao", allCategory.get(0).nome());

		assertEquals(2L, allCategory.get(1).id());
		assertEquals("Recepcao", allCategory.get(1).nome());

		Mockito.verify(categoriaRepository, Mockito.times(1)).findAll();
	}

	@Test
	@DisplayName("Deve retornar uma lista vazia")
	public void deveRetornarUmaListaVazia() {

		Mockito.when(categoriaService.findAll()).thenReturn(List.of());

		List<CategoriaResponseDTO> allCategory = categoriaService.findAll();

		assertTrue(allCategory.isEmpty());
		Mockito.verify(categoriaRepository).findAll();
	}

	@Test
	@DisplayName("Deve retornar uma Categoria")
	public void deveRetornarUmaCategoria() {

		Categoria categoria = new Categoria(1L, "Desenvolvimento", null);

		Mockito.when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

		CategoriaResponseDTO buscaCategoria = categoriaService.buscar(1L);

		assertNotNull(buscaCategoria);

		assertEquals(1L, buscaCategoria.id());
		assertEquals("Desenvolvimento", buscaCategoria.nome());

		Mockito.verify(categoriaRepository, Mockito.times(1)).findById(1L);

	}

	@Test
	@DisplayName("Deve Criar uma Categoria")
	public void deveCriarUmaCategoria() throws Throwable {

		// Criando uma categoriaDTO
		CategoriaRequestDto categoriaRequestDto = new CategoriaRequestDto("Hardware");

		Mockito.when(categoriaRepository.findByNome(categoriaRequestDto.nome())).thenReturn(Optional.empty());
		Mockito.when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));

		CategoriaResponseDTO criaCategoria = categoriaService.criar(categoriaRequestDto);

		assertNotNull(criaCategoria);
		assertEquals("Hardware", criaCategoria.nome());
		verify(categoriaRepository, Mockito.times(1)).findByNome("Hardware");
		verify(categoriaRepository, Mockito.times(1)).save(any(Categoria.class));

	}

	@Test
	@DisplayName("Deve Lancar Excecao Quando Categoria Ja Existe")
	public void deveLancarExcecaoQuandoCategoriaJaExiste() {

		CategoriaRequestDto dto = new CategoriaRequestDto("Eletronicos");

		Categoria categoriaExistente = new Categoria(1L, "Eletronicos", null);

		when(categoriaRepository.findByNome(dto.nome())).thenReturn(Optional.of(categoriaExistente));

		assertThrows(CategoriaJaExisteException.class, () -> categoriaService.criar(dto));

		verify(categoriaRepository).findByNome(dto.nome());
		verify(categoriaRepository, never()).save(any(Categoria.class));

	}

	@Test
	@DisplayName("Deve Buscar Chamados Por Categoria")
	public void deveBuscarChamadosPorCategoria() {

		List<Chamado> chamados = new ArrayList<>();
		Categoria categoriaDesenvolvimento = new Categoria(1L, "Desenvolvimento", chamados);

		Chamado chamadoErroSite = new Chamado(1L, "Site com erro 500", "site nao abre", categoriaDesenvolvimento);
		Chamado chamadoErroSistema = new Chamado(2L, "Windows nao Abre", "erro usuario invalido",
				categoriaDesenvolvimento);

		chamados.add(chamadoErroSite);
		chamados.add(chamadoErroSistema);

		when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaDesenvolvimento));

		when(chamadoRepository.findByCategoria(categoriaDesenvolvimento)).thenReturn(chamados);

		List<ChamadoResponseDTO> chamadosPorCategoria = categoriaService.buscarChamadosPorCategoria(1L);

		assertNotNull(chamadosPorCategoria);
		assertEquals(2, chamadosPorCategoria.size());

		assertEquals(1L, chamadosPorCategoria.get(0).id());
		assertEquals("Desenvolvimento", chamadosPorCategoria.get(0).categoriaNome());
		assertEquals("Site com erro 500", chamadosPorCategoria.get(0).titulo());

		assertEquals(2L, chamadosPorCategoria.get(1).id());
		assertEquals("Desenvolvimento", chamadosPorCategoria.get(1).categoriaNome());
		assertEquals("Windows nao Abre", chamadosPorCategoria.get(1).titulo());

		verify(categoriaRepository, times(1)).findById(1L);
		verify(chamadoRepository, times(1)).findByCategoria(categoriaDesenvolvimento);
		verifyNoMoreInteractions(categoriaRepository, chamadoRepository);
	}

	@Test
	@DisplayName("Deve lancar excecao quando Categoria Nao Encontrada ")
	public void deveLancarExcecaoQuandoCategoriaNaoEncontrada() {

		when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(CategoriaNaoEncontradaException.class, () -> categoriaService.buscarChamadosPorCategoria(1L));

		verify(categoriaRepository).findById(1L);
		verifyNoMoreInteractions(categoriaRepository, chamadoRepository);
	}

	@Test
	@DisplayName("Deve Excluir Uma Categoria")
	public void deveExcluirUmaCategoria() {

		when(categoriaRepository.existsById(1L)).thenReturn(true);
		when(chamadoRepository.existsByCategoriaId(1L)).thenReturn(false);

		categoriaService.excluirCategoria(1L);

		verify(categoriaRepository, times(1)).deleteById(1L);

	}

}
