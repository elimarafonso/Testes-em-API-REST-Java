package com.techdesk.techdesk.categorias.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techdesk.techdesk.categorias.entity.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

   // List<Categoria> findByNome(String nome);

	 Optional<Categoria> findByNome(String nome);


	
}
