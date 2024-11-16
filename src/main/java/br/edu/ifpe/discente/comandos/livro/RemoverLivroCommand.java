package br.edu.ifpe.discente.comandos.livro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.utils.Constante;

public class RemoverLivroCommand implements Command {
	
	private LivroDAO dao;

	public RemoverLivroCommand(LivroDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		this.dao.remover(Integer.parseInt(request.getParameter("id")));
		request.setAttribute("livros", dao.listarTodos());
		return Constante.LISTA_LIVROS;
	}

}
