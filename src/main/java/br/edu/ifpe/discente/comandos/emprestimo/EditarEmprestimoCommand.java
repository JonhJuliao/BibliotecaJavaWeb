package br.edu.ifpe.discente.comandos.emprestimo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.utils.Constante;

public class EditarEmprestimoCommand implements Command {

	private EmprestimoDAO emprestimoDAO;
	private UsuarioDAO usuarioDAO;
	private LivroDAO livroDAO;

	public EditarEmprestimoCommand(EmprestimoDAO emprestimoDAO, UsuarioDAO usuarioDAO, LivroDAO livroDAO) {
		super();
		this.emprestimoDAO = emprestimoDAO;
		this.usuarioDAO = usuarioDAO;
		this.livroDAO = livroDAO;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var id = request.getParameter("id");
		var emprestimo = emprestimoDAO.listarPorId(Integer.parseInt(id));
		request.setAttribute("emprestimo", emprestimo);
		var usuarios = usuarioDAO.listarTodos();
		request.setAttribute("usuarios", usuarios);
		var livros = livroDAO.listarTodos();
		request.setAttribute("livros", livros);
		return Constante.CONTROLA_EMPRESTIMOS;
	}

}
