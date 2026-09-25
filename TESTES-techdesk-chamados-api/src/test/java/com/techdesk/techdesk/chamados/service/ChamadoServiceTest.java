package com.techdesk.techdesk.chamados.service;

import com.techdesk.techdesk.categorias.entity.Categoria;
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

import java.util.Optional;

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

        Categoria categoria = new Categoria(1L,"hardware", null);

        ChamadoRequestDTO chamadoRequestDTO = new ChamadoRequestDTO("Impressora nao liga",
                                                                    "não consigo ligar a impressora",
                                                                    1L);

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


}