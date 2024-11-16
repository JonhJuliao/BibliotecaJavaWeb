package br.edu.ifpe.discente.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.infra.db.DBConnection;
import br.edu.ifpe.discente.infra.db.MySQLDBConnection;
import br.edu.ifpe.discente.service.EmprestimoService;
import br.edu.ifpe.discente.service.UsuarioService;
import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.comandos.emprestimo.AtualizarEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.CadastrarEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.DevolverEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.EditarEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.ListarEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.NovoEmprestimoCommand;
import br.edu.ifpe.discente.comandos.emprestimo.RemoverEmprestimoCommand;
import br.edu.ifpe.discente.comandos.livro.AtualizarLivroCommand;
import br.edu.ifpe.discente.comandos.livro.CadastrarLivroCommand;
import br.edu.ifpe.discente.comandos.livro.EditarLivroCommand;
import br.edu.ifpe.discente.comandos.livro.ListarLivroCommand;
import br.edu.ifpe.discente.comandos.livro.NovoLivroCommand;
import br.edu.ifpe.discente.comandos.livro.RemoverLivroCommand;
import br.edu.ifpe.discente.comandos.usuario.AtualizarUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.CadastrarUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.EditarUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.ListarUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.NovoUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.PagarMultaUsuarioCommand;
import br.edu.ifpe.discente.comandos.usuario.RemoverUsuarioCommand;

@WebServlet("/app")
public class BibliotecaServlet extends HttpServlet implements Serializable{
	private static final long serialVersionUID = 1L;
	private DBConnection connection = new MySQLDBConnection();
	private LivroDAO livroDAO = new LivroDAO(connection);
	private UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
	private EmprestimoDAO emprestimoDAO = new EmprestimoDAO(connection, livroDAO, usuarioDAO);
	private EmprestimoService emprestimoService = new EmprestimoService(emprestimoDAO, livroDAO, usuarioDAO);
	private UsuarioService usuarioService = new UsuarioService(usuarioDAO);
	private Map<String, Command> comandos = new HashMap<>();
	
	public BibliotecaServlet() {
		comandos.put("listarLivros", new ListarLivroCommand(livroDAO));
		comandos.put("novoLivro", new NovoLivroCommand());
		comandos.put("cadastrarLivro", new CadastrarLivroCommand(livroDAO));
		comandos.put("editarLivro", new EditarLivroCommand(livroDAO));
		comandos.put("atualizarLivro", new AtualizarLivroCommand(livroDAO));
		comandos.put("removerLivro", new RemoverLivroCommand(livroDAO));
		comandos.put("listarEmprestimos", new ListarEmprestimoCommand(emprestimoDAO));
		comandos.put("novoEmprestimo", new NovoEmprestimoCommand(usuarioDAO, livroDAO));
		comandos.put("cadastrarEmprestimo", new CadastrarEmprestimoCommand(emprestimoService,emprestimoDAO,usuarioDAO,livroDAO));
		comandos.put("editarEmprestimo", new EditarEmprestimoCommand(emprestimoDAO, usuarioDAO, livroDAO));
		comandos.put("atualizarEmprestimo", new AtualizarEmprestimoCommand(emprestimoService,emprestimoDAO,usuarioDAO,livroDAO));
		comandos.put("removerEmprestimo", new RemoverEmprestimoCommand(emprestimoDAO));
		comandos.put("devolverEmprestimo", new DevolverEmprestimoCommand(emprestimoService, emprestimoDAO));
		comandos.put("listarUsuarios", new ListarUsuarioCommand(usuarioDAO));
		comandos.put("novoUsuario", new NovoUsuarioCommand());
		comandos.put("cadastrarUsuario", new CadastrarUsuarioCommand(usuarioDAO));
		comandos.put("editarUsuario", new EditarUsuarioCommand(usuarioDAO));
		comandos.put("atualizarUsuario", new AtualizarUsuarioCommand(usuarioDAO));
		comandos.put("removerUsuario", new RemoverUsuarioCommand(usuarioDAO));
		comandos.put("pagarMultaUsuario", new PagarMultaUsuarioCommand(usuarioDAO, usuarioService));
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String pagina = this.processRequest(request, response);
		request.getRequestDispatcher(pagina).forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		var pagina = processRequest(request, response);
		response.sendRedirect(pagina);
		//request.getRequestDispatcher(pagina).forward(request, response);

	}
	
	private String processRequest(HttpServletRequest request, HttpServletResponse response) {
		Command command = this.comandos.get(request.getParameter("acao"));
		// recurso geralmente é uma pgina
		return command.execute(request, response);
	}

}
