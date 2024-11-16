package br.edu.ifpe.discente.comandos.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class EditarUsuarioCommand implements Command {

	private UsuarioDAO dao;
	
	public EditarUsuarioCommand(UsuarioDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var id = request.getParameter("id");
		var usuario = dao.listarPorId(Integer.parseInt(id));
		request.setAttribute("usuario", usuario);
		return Constante.CONTROLA_USUARIOS;
	}

}
