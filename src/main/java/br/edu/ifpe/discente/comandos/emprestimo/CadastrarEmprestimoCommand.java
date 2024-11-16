package br.edu.ifpe.discente.comandos.emprestimo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.entity.Emprestimo;
import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.service.EmprestimoService;
import br.edu.ifpe.discente.utils.Constante;

public class CadastrarEmprestimoCommand implements Command {
    
    private EmprestimoService emprestimoService;
    private EmprestimoDAO emprestimoDAO;
    private UsuarioDAO usuarioDAO;
    private LivroDAO livroDAO;

    public CadastrarEmprestimoCommand(EmprestimoService emprestimoService, EmprestimoDAO emprestimoDAO,
			UsuarioDAO usuarioDAO, LivroDAO livroDAO) {
		super();
		this.emprestimoService = emprestimoService;
		this.emprestimoDAO = emprestimoDAO;
		this.usuarioDAO = usuarioDAO;
		this.livroDAO = livroDAO;
	}

	@Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
        
        // Verifica se o usuário já possui um empréstimo ativo
        if (emprestimoDAO.verificarEmprestimoAtivo(usuarioId)) {
            throw new RuntimeException("O usuário já possui um empréstimo ativo! Só é permitido um empréstimo ativo por usuário!");
        }

        Usuario usuario = usuarioDAO.listarPorId(usuarioId);

        // Verifica se o usuário tem multa pendente
        emprestimoService.verificaMultaPendente(usuario);
        
        String[] livroIdsParam = request.getParameterValues("livroIds");

        // Verifica se foi selecionado pelo menos um livro
        if (livroIdsParam == null || livroIdsParam.length == 0) {
            throw new RuntimeException("Nenhum livro selecionado para o empréstimo!");
        }

        List<Livro> livros = new ArrayList<>();
        for (String livroIdStr : livroIdsParam) {
            int livroId = Integer.parseInt(livroIdStr);
            Livro livro = livroDAO.listarPorId(livroId);
            livros.add(livro);
        }

        // Valida e salva o empréstimo
        Emprestimo emprestimo = new Emprestimo(usuario, livros);
        emprestimoService.validarESalvarEmprestimo(emprestimo);

        return request.getContextPath() + Constante.ACAO_LISTAR_EMPRESTIMOS;
    }
}
