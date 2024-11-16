package br.edu.ifpe.discente.comandos.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class RemoverUsuarioCommand implements Command {

	private UsuarioDAO dao;
	
	public RemoverUsuarioCommand(UsuarioDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		this.dao.remover(Integer.parseInt(request.getParameter("id")));
		request.setAttribute("usuarios", dao.listarTodos());
		return Constante.LISTA_USUARIOS;
	}

}
