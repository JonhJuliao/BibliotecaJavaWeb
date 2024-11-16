package br.edu.ifpe.discente.comandos.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.utils.Constante;

public class NovoUsuarioCommand implements Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return Constante.CONTROLA_USUARIOS;
	}

}
