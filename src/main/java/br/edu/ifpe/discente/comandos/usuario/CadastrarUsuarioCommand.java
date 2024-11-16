package br.edu.ifpe.discente.comandos.usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class CadastrarUsuarioCommand implements Command {
	
	private UsuarioDAO dao;

	public CadastrarUsuarioCommand(UsuarioDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String nome = request.getParameter("nome");
		String aniversarioStr = request.getParameter("aniversario");
		if(aniversarioStr != null && !aniversarioStr.isEmpty() && nome != null && !nome.isEmpty()) {
			LocalDate aniversario = LocalDate.parse(aniversarioStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			dao.salvar(new Usuario(nome, aniversario));
		}else {
			throw new RuntimeException("O usuário deve possuir nome e data de aniversario!");
		}	
		return request.getContextPath() + Constante.ACAO_LISTAR_USUARIOS;
	}

}
