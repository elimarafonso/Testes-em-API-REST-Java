package com.techdesk.techdesk.categorias.entity;


import java.util.List;

import com.techdesk.techdesk.chamados.entity.Chamado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false,unique = true)
    private String nome;


    @OneToMany(mappedBy = "categoria")
    private List<Chamado> chamados;
    
    
    public Categoria() {}

	public Categoria(Long id, String nome, List<Chamado> chamados) {
		this.id = id;
		this.nome = nome;
		this.chamados = chamados;
	}
    
    public Long getId() {return id; }

    public String getNome() { return nome; }

    public void setId(Long id) {this.id = id;}

    public void setNome(String nome) { this.nome = nome; }



	@Override
	public String toString() {
		return "Categoria [nome=" + nome + "]";
	}


    
    
}
