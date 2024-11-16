package br.edu.ifpe.discente.comandos.emprestimo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.utils.Constante;

public class RemoverEmprestimoCommand implements Command {
	
	private EmprestimoDAO dao;
	
	public RemoverEmprestimoCommand(EmprestimoDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		this.dao.remover(Integer.parseInt(request.getParameter("id")));
		request.setAttribute("emprestimos", dao.listarTodos());
		return Constante.LISTA_EMPRESTIMOS;
	}

}
