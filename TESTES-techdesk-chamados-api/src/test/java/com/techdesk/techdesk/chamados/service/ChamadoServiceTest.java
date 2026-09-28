package com.techdesk.techdesk.chamados.service;

import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.categorias.exception.CategoriaNaoEncontradaException;
import com.techdesk.techdesk.categorias.repository.CategoriaRepository;
import com.techdesk.techdesk.chamados.dto.ChamadoRequestDTO;
import com.techdesk.techdesk.chamados.dto.ChamadoResponseDTO;
import com.techdesk.techdesk.chamados.entity.Chamado;
import com.techdesk.techdesk.chamados.entity.StatusChamado;
import com.techdesk.techdesk.chamados.repository.ChamadoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

    @InjectMocks
    private ChamadoService chamadoService;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ChamadoRepository chamadoRepository;


    @Test
    @DisplayName("Deve Criar um Chamado")
    void deveCriarUmChamado() {

        Categoria categoria = new Categoria(1L, "hardware", null);

        ChamadoRequestDTO chamadoRequestDTO = new ChamadoRequestDTO("Impressora nao liga", "não consigo ligar a impressora", 1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        //"Quando save() for chamado, devolva o próprio objeto Chamado que recebeu."
        when(chamadoRepository.save(any(Chamado.class))).thenAnswer(inv -> inv.getArgument(0));

        ChamadoResponseDTO chamadoCriado = chamadoService.criar(chamadoRequestDTO);

        assertThat(chamadoCriado).isNotNull();
        assertThat(chamadoCriado.titulo()).isEqualTo(chamadoRequestDTO.titulo());
        assertThat(chamadoCriado.descricao()).isEqualTo(chamadoRequestDTO.descricao());
        assertThat(chamadoCriado.categoriaNome()).isEqualTo(categoria.getNome());
        assertThat(chamadoCriado.status()).isEqualTo(StatusChamado.ABERTO);

        verify(categoriaRepository, times(1)).findById(1L);
        verify(chamadoRepository, times(1)).save(any(Chamado.class));


    }

    @Test
    @DisplayName("Deve lançar exceção quando categoria não existir")
    void deveLancarExcecaoQuandoCategoriaNaoExistir() {

        ChamadoRequestDTO chamadoRequestDTO = new ChamadoRequestDTO("Impressora nao liga", "não consigo ligar a impressora", 1L);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> chamadoService.criar(chamadoRequestDTO)).isInstanceOf(CategoriaNaoEncontradaException.class);

        verify(categoriaRepository).findById(1L);
        verify(chamadoRepository, never()).save(any(Chamado.class));

    }

    @Test
    @DisplayName("Deve Criar Chamados Em Lote")
    void deveCriarChamadosEmLote() {


        Categoria categoriaSoftware = new Categoria(1L, "Software", null);
        Categoria categoriaHardware = new Categoria(2L, "hardware", null);

        ChamadoRequestDTO chamadoSoftware = new ChamadoRequestDTO("Programa não abre", "Não acho o icone do programa", categoriaSoftware.getId());
        ChamadoRequestDTO chamadoHardware = new ChamadoRequestDTO("Computador não liga", "não consigo ligar a computador", categoriaHardware.getId());

        List<ChamadoRequestDTO> dtos = List.of(chamadoSoftware, chamadoHardware);

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoriaSoftware));
        when(categoriaRepository.findById(2L))
                .thenReturn(Optional.of(categoriaHardware));

        when(chamadoRepository.saveAll(anyList()))
                .thenAnswer(inv -> inv.getArgument(0));

        List<ChamadoResponseDTO> chamadosCriadosDTOS = chamadoService.criarEmLote(dtos);

        assertThat(chamadosCriadosDTOS).isNotNull();
        assertThat(chamadosCriadosDTOS.size()).isEqualTo(2);

        assertThat(chamadosCriadosDTOS.getFirst().titulo())
                .isEqualTo(chamadoSoftware.titulo());
        assertThat(chamadosCriadosDTOS.getFirst().descricao())
                .isEqualTo(chamadoSoftware.descricao());
        assertThat(chamadosCriadosDTOS.getFirst().categoriaNome())
                .isEqualTo(categoriaSoftware.getNome());
        assertThat(chamadosCriadosDTOS.getFirst().status())
                .isEqualTo(StatusChamado.ABERTO);
        assertThat(chamadosCriadosDTOS.getFirst().dataAbertura())
                .isNotNull();

        assertThat(chamadosCriadosDTOS.get(1).titulo())
                .isEqualTo(chamadoHardware.titulo());
        assertThat(chamadosCriadosDTOS.get(1).descricao())
                .isEqualTo(chamadoHardware.descricao());
        assertThat(chamadosCriadosDTOS.get(1).categoriaNome())
                .isEqualTo(categoriaHardware.getNome());
        assertThat(chamadosCriadosDTOS.get(1).status())
                .isEqualTo(StatusChamado.ABERTO);
        assertThat(chamadosCriadosDTOS.get(1).dataAbertura())
                .isNotNull();

        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).findById(2L);
        verify(chamadoRepository).saveAll(anyList());

    }
    @Test
    @DisplayName("Nao Deve Salvar Nenhum Chamado Quando Uma Categoria Nao Existir")
    void naoDeveSalvarNenhumChamadoQuandoUmaDasCategoriasNaoExistir(){
        Categoria categoriaSoftware = new Categoria(1L, "Software", null);

        ChamadoRequestDTO chamadoSoftware = new ChamadoRequestDTO("Programa não abre", "Não acho o icone do programa", categoriaSoftware.getId());
        ChamadoRequestDTO chamadoNaoExiste = new ChamadoRequestDTO("Computador não liga", "não consigo ligar a computador", 2L);

        List<ChamadoRequestDTO> dtos = List.of(chamadoSoftware);

        when(categoriaRepository.findById(1L))
                .thenReturn(Optional.of(categoriaSoftware));
        when(categoriaRepository.findById(2L)).thenReturn(Optional.empty());

        when(chamadoRepository.saveAll(anyList()))
                .thenAnswer(inv -> inv.getArgument(0));

        List<ChamadoResponseDTO> chamadoResponseDTOS = chamadoService.criarEmLote(dtos);

    }

    @Test
    @DisplayName("Deve Lancar Uma Categoria Nao Encontrada Exception")
    void deveLancarUmaCategoriaNaoEncontradaException() {

        List<ChamadoRequestDTO> dtos = new ArrayList<>();

        ChamadoRequestDTO chamadoSoftware =
                new ChamadoRequestDTO(
                        "Programa não abre",
                        "Não acho o icone do programa",
                        1L);

        dtos.add(chamadoSoftware);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                chamadoService.criarEmLote(dtos))
                .isInstanceOf(CategoriaNaoEncontradaException.class);

        verify(categoriaRepository).findById(1L);
        verify(chamadoRepository, never()).saveAll(anyList());

    }

    @Test
    @DisplayName("Deve Retornar Uma Lista De Chamados")
    void deveRetornarUmaListaDeChamados() {

        Categoria categoriaSoftware = new Categoria(1L, "Software", null);
        Categoria categoriaHardware = new Categoria(2L, "hardware", null);


        Chamado chamadoSoftware = new Chamado(1L, "Programa não abre", "Não acho o icone do programa", categoriaSoftware);
        Chamado chamadoHardware = new Chamado(2L, "Computador não liga", "não consigo ligar a computador", categoriaHardware);

        List<Chamado> chamados = List.of(chamadoSoftware, chamadoHardware);

        when(chamadoRepository.findAll()).thenReturn(chamados);

        List<ChamadoResponseDTO> chamadoResponseDTOS = chamadoService.listarTodos();

        assertThat(chamadoResponseDTOS).isNotNull();
        assertThat(chamadoResponseDTOS).hasSize(2);
        assertThat(chamadoResponseDTOS.getFirst().titulo()).isEqualTo(chamadoSoftware.getTitulo());
        assertThat(chamadoResponseDTOS.getFirst().descricao()).isEqualTo(chamadoSoftware.getDescricao());
        assertThat(chamadoResponseDTOS.getFirst().categoriaNome()).isEqualTo(chamadoSoftware.getCategoria().getNome());

        assertThat(chamadoResponseDTOS.get(1).titulo()).isEqualTo(chamadoHardware.getTitulo());
        assertThat(chamadoResponseDTOS.get(1).descricao()).isEqualTo(chamadoHardware.getDescricao());
        assertThat(chamadoResponseDTOS.get(1).categoriaNome()).isEqualTo(chamadoHardware.getCategoria().getNome());

        verify(chamadoRepository, times(1)).findAll();

    }


}