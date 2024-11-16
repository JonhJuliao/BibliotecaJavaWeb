package br.edu.ifpe.discente.domain.entity;

import java.io.Serializable;
import java.util.Objects;

public class Livro implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String titulo;
	private String autor;
	private int anoPublicacao;
	private int quantidade;
	
	public Livro() {
		super();
	}

	public Livro(String titulo, String autor, int anoPublicacao, int quantidade) {
		super();
		this.titulo = titulo;
		this.autor = autor;
		this.anoPublicacao = anoPublicacao;
		this.quantidade = quantidade;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getAnoPublicacao() {
		return anoPublicacao;
	}

	public void setAnoPublicacao(int anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anoPublicacao, autor, id, quantidade, titulo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Livro other = (Livro) obj;
		return anoPublicacao == other.anoPublicacao && Objects.equals(autor, other.autor) && id == other.id
				&& quantidade == other.quantidade && Objects.equals(titulo, other.titulo);
	}

}
