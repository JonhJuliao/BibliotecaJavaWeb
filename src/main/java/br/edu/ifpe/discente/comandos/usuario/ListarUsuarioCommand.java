package br.edu.ifpe.discente.comandos.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class ListarUsuarioCommand implements Command {
	
	private UsuarioDAO dao;

	public ListarUsuarioCommand(UsuarioDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var usuarios = dao.listarTodos();
		request.setAttribute("usuarios", usuarios);
		return Constante.LISTA_USUARIOS;
	}

}
