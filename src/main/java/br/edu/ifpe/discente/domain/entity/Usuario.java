package br.edu.ifpe.discente.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Usuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String nome;
	private LocalDate aniversario;
	private BigDecimal multaPendente;
	
	public Usuario() {
		super();
		this.multaPendente = BigDecimal.ZERO;
	}

	public Usuario(String nome, LocalDate aniversario) {
		super();
		this.nome = nome;
		this.aniversario = aniversario;
		this.multaPendente = BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getAniversario() {
		return aniversario;
	}

	public void setAniversario(LocalDate aniversario) {
		this.aniversario = aniversario;
	}

	public BigDecimal getMultaPendente() {
		return multaPendente;
	}

	public void setMultaPendente(BigDecimal multaPendente) {
		this.multaPendente = multaPendente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aniversario, id, multaPendente, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(aniversario, other.aniversario) && id == other.id
				&& Objects.equals(multaPendente, other.multaPendente) && Objects.equals(nome, other.nome);
	}	
}
