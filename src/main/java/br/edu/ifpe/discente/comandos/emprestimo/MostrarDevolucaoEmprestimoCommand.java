package br.edu.ifpe.discente.comandos.emprestimo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.utils.Constante;

public class MostrarDevolucaoEmprestimoCommand implements Command {
	
	private EmprestimoDAO dao;
	

	public MostrarDevolucaoEmprestimoCommand(EmprestimoDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var id = request.getParameter("id");
		var emprestimo = dao.listarPorId(Integer.parseInt(id));
		request.setAttribute("emprestimo", emprestimo);
		return Constante.DEVOLVER_EMPRESTIMO;
	}

}
