package br.edu.ifpe.discente.comandos.emprestimo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.utils.Constante;

public class ListarEmprestimoCommand implements Command {
	
	private EmprestimoDAO dao;
	
	public ListarEmprestimoCommand(EmprestimoDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var emprestimos = dao.listarTodos();
		request.setAttribute("emprestimos", emprestimos);
		return Constante.LISTA_EMPRESTIMOS;
	}

}
