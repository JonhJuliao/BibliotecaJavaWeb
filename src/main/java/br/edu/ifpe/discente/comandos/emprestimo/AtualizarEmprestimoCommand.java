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

public class AtualizarEmprestimoCommand implements Command {

    private EmprestimoService emprestimoService;
    private EmprestimoDAO emprestimoDAO;
    private UsuarioDAO usuarioDAO;
    private LivroDAO livroDAO;

    public AtualizarEmprestimoCommand(EmprestimoService emprestimoService, EmprestimoDAO emprestimoDAO, UsuarioDAO usuarioDAO, LivroDAO livroDAO) {
        this.emprestimoService = emprestimoService;
        this.emprestimoDAO = emprestimoDAO;
        this.usuarioDAO = usuarioDAO;
        this.livroDAO = livroDAO;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int emprestimoId = Integer.parseInt(request.getParameter("emprestimoId"));
        Emprestimo emprestimoAtual = emprestimoDAO.listarPorId(emprestimoId);

        if (emprestimoAtual == null) {
            throw new RuntimeException("Empréstimo não encontrado.");
        }

        int usuarioId = Integer.parseInt(request.getParameter("usuarioId"));
        Usuario usuario = usuarioDAO.listarPorId(usuarioId);
        emprestimoAtual.setUsuario(usuario);

        String[] livroIdsParam = request.getParameterValues("livroIds");
        if (livroIdsParam == null || livroIdsParam.length == 0) {
            throw new RuntimeException("Nenhum livro selecionado para o empréstimo!");
        }

        List<Livro> livrosAtualizados = new ArrayList<>();
        for (String livroIdStr : livroIdsParam) {
            int livroId = Integer.parseInt(livroIdStr);
            Livro livro = livroDAO.listarPorId(livroId);
            livrosAtualizados.add(livro);
        }

        Emprestimo emprestimoAtualizado = new Emprestimo(usuario, livrosAtualizados);
        emprestimoAtualizado.setId(emprestimoId);
        emprestimoAtualizado.setDataAquisicao(emprestimoAtual.getDataAquisicao());
        emprestimoAtualizado.setDataDevolucao(emprestimoAtual.getDataDevolucao());
        emprestimoAtualizado.setDataDevolucaoReal(emprestimoAtual.getDataDevolucaoReal());

        // Chama o service para validar e atualizar o empréstimo com as regras de negócio
        emprestimoService.validarEAtualizarEmprestimo(emprestimoAtual, emprestimoAtualizado);

        return request.getContextPath() + Constante.ACAO_LISTAR_EMPRESTIMOS;
    }
}
