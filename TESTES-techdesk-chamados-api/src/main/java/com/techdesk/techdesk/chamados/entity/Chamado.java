package com.techdesk.techdesk.chamados.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.techdesk.techdesk.categorias.entity.Categoria;
import com.techdesk.techdesk.tecnicos.entity.Tecnico;
import com.techdesk.techdesk.usuarios.entity.Usuario;

@Entity
@Table(name = "chamados")
public class Chamado {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	private String titulo;

	@Column(length = 100, nullable = false)
	private String descricao;

	@Enumerated(EnumType.STRING) // salva o enum como texto legível ("ABERTO"), não como número
	private StatusChamado statusChamado;

	private LocalDateTime dataAbertura;
	private LocalDateTime dataFechamento;

	// Relacionamento N:1 — vários chamados pertencem a UMA categoria
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;

	// Relacionamento N:1 — vários chamados podem ser atendidos por UM técnico
	// @ManyToOne
	// @JoinColumn(name = "tecnico_id")
	// private Tecnico tecnico;

	// @ManyToOne
	// @JoinColumn(name = "usuario_id", nullable = false)
	// private Usuario usuario;

	public Chamado() {
	}

	public Chamado(Long id, String titulo, String descricao, StatusChamado statusChamado, LocalDateTime dataAbertura,
			LocalDateTime dataFechamento, Categoria categoria, Tecnico tecnico, Usuario usuario) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.statusChamado = statusChamado;
		this.dataAbertura = dataAbertura;
		this.dataFechamento = dataFechamento;
		this.categoria = categoria;
		// this.tecnico = tecnico;
		// this.usuario = usuario;
	}

	public Chamado(Long id, String titulo, String descricao, Categoria categoria) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.categoria = categoria;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setStatusChamado(StatusChamado statusChamado) {
		this.statusChamado = statusChamado;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public void setDataFechamento(LocalDateTime dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public StatusChamado getStatusChamado() {
		return statusChamado;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public LocalDateTime getDataFechamento() {
		return dataFechamento;
	}



	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}
	

}
