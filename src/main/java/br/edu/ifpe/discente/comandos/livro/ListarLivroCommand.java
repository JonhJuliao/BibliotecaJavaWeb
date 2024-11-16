package br.edu.ifpe.discente.comandos.livro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.utils.Constante;

public class ListarLivroCommand implements Command {
	
	private LivroDAO dao;

	public ListarLivroCommand(LivroDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var livros = dao.listarTodos();
		request.setAttribute("livros", livros);
		return Constante.LISTA_LIVROS;
	}

}
