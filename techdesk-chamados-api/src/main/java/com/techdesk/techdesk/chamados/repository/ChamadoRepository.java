package com.techdesk.techdesk.chamados.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.chamados.entity.Chamado;
import com.techdesk.techdesk.chamados.entity.StatusChamado;

//📁 techdesk-chamados-api/src/main/java/com/techdesk/chamados/repository/ChamadoRepository.java

//Repository: o Spring Data JPA gera as queries automaticamente a partir
//da ASSINATURA do método — não escrevemos SQL manual aqui.
public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

	
	List<Chamado> findByStatusChamado(StatusChamado statusChamado);
	// List<Chamado> findByTecnicoId(Long tecnicoId);
 
	boolean existsByCategoriaId(Long categoriaId);

	List<Chamado> findByCategoria(Categoria categoria);
	

 
}