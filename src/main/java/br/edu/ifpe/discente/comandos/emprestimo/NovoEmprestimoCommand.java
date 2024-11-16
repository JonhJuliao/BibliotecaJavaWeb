package br.edu.ifpe.discente.comandos.emprestimo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class NovoEmprestimoCommand implements Command {
	
	private UsuarioDAO usuarioDAO;
	private LivroDAO livroDAO;

	public NovoEmprestimoCommand(UsuarioDAO usuarioDAO, LivroDAO livroDAO) {
		super();
		this.usuarioDAO = usuarioDAO;
		this.livroDAO = livroDAO;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var usuarios = usuarioDAO.listarTodos();
		request.setAttribute("usuarios", usuarios);
		var livros = livroDAO.listarTodos();
		request.setAttribute("livros", livros);
		return Constante.CONTROLA_EMPRESTIMOS;
	}

}
