package br.edu.ifpe.discente.domain.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Emprestimo implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	private Usuario usuario;
	private List<Livro> livros = new ArrayList<>();
	private LocalDate dataAquisicao;
	private LocalDate dataDevolucao; //Esse atributo seta o dia marcado para devolver
	private LocalDate dataDevolucaoReal; //Esse atributo seta o dia que realmente foi devolvido
	private BigDecimal multa;
	
	public Emprestimo() {
		super();
		this.dataAquisicao = LocalDate.now();
		this.dataDevolucao = dataAquisicao.plusDays(3);
		this.multa = BigDecimal.ZERO;
	}
	
	public Emprestimo(Usuario usuario, List<Livro> livros) {
		super();
		this.usuario = usuario;
		this.livros = livros;
		this.dataAquisicao = LocalDate.now();
		this.dataDevolucao = dataAquisicao.plusDays(3);
		this.multa = BigDecimal.ZERO;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public LocalDate getDataAquisicao() {
		return dataAquisicao;
	}

	public void setDataAquisicao(LocalDate dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	
	public LocalDate getDataDevolucaoReal() {
		return dataDevolucaoReal;
	}

	public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
		this.dataDevolucaoReal = dataDevolucaoReal;
	}

	public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

	public void addLivro(Livro livro) {
		this.livros.add(livro);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dataAquisicao, dataDevolucao, dataDevolucaoReal, id, livros, multa, usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emprestimo other = (Emprestimo) obj;
		return Objects.equals(dataAquisicao, other.dataAquisicao) && Objects.equals(dataDevolucao, other.dataDevolucao)
				&& Objects.equals(dataDevolucaoReal, other.dataDevolucaoReal) && id == other.id
				&& Objects.equals(livros, other.livros) && Objects.equals(multa, other.multa)
				&& Objects.equals(usuario, other.usuario);
	}	
}
