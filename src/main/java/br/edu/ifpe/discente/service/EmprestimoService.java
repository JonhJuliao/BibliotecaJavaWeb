package br.edu.ifpe.discente.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.ifpe.discente.domain.entity.Emprestimo;
import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;

public class EmprestimoService {
    
    private EmprestimoDAO emprestimoDAO;
    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, LivroDAO livroDAO, UsuarioDAO usuarioDAO) {
        this.emprestimoDAO = emprestimoDAO;
        this.livroDAO = livroDAO;
        this.usuarioDAO = usuarioDAO;
    }

	public void validarESalvarEmprestimo(Emprestimo emprestimo) {
        validarRegrasEmprestimo(emprestimo);
        emprestimoDAO.salvar(emprestimo);
        reduzirEstoqueDeLivros(emprestimo.getLivros());
    }
    
    public void validarEAtualizarEmprestimo(Emprestimo emprestimoAtual, Emprestimo emprestimoAtualizado) {
        validarRegrasEmprestimo(emprestimoAtualizado);
        ajustarEstoque(emprestimoAtual, emprestimoAtualizado);
        emprestimoDAO.atualizar(emprestimoAtualizado);
    }
    
    public void ajustarEstoque(Emprestimo emprestimoAtual, Emprestimo emprestimoAtualizado) {
    	// Restaurar o estoque dos livros do empréstimo antigo
        for (Livro livro : emprestimoAtual.getLivros()) {
            Livro livroNoBanco = livroDAO.listarPorId(livro.getId());
            livroNoBanco.setQuantidade(livroNoBanco.getQuantidade() + 1);
            livroDAO.ajustarEstoque(livro.getId(), 1);
        }

        // Verificar e reduzir o estoque dos novos livros
        for (Livro livro : emprestimoAtualizado.getLivros()) {
            Livro livroNoBanco = livroDAO.listarPorId(livro.getId());
            if (livroNoBanco.getQuantidade() <= 0) {
                throw new RuntimeException("O livro '" + livro.getTitulo() + "' não possui estoque disponível.");
            }
            livroNoBanco.setQuantidade(livroNoBanco.getQuantidade() - 1);
            livroDAO.ajustarEstoque(livro.getId(), -1);
        }
		
	}

	private void validarRegrasEmprestimo(Emprestimo emprestimo) {
        validarQuantidadeDeLivros(emprestimo.getLivros());
        validarLivrosUnicos(emprestimo.getLivros());
        verificarEstoque(emprestimo.getLivros());
    }

    public void verificaMultaPendente(Usuario usuario) {
        if (usuario.getMultaPendente().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("O usuário tem multa pendente e portanto não pode realizar um novo empréstimo!");
        }
    }

    public void validarQuantidadeDeLivros(List<Livro> livros) {
        if (livros.size() > 3) {
            throw new RuntimeException("Não é permitido pegar mais do que 3 livros emprestados de uma vez!");
        }
    }

    public void validarLivrosUnicos(List<Livro> livros) {
        Set<Integer> livroIds = new HashSet<>();
        for (Livro livro : livros) {
            if (!livroIds.add(livro.getId())) {
                throw new RuntimeException("Não é permitido levar mais de uma cópia do mesmo livro!");
            }
        }
    }

    private void verificarEstoque(List<Livro> livros) {
        for (Livro livro : livros) {
            if (livroDAO.listarPorId(livro.getId()).getQuantidade() <= 0) {
                throw new RuntimeException("O livro '" + livro.getTitulo() + "' não possui estoque disponível.");
            }
        }
    }
    
    private void reduzirEstoqueDeLivros(List<Livro> livros) {
    	for (Livro livro: livros) {
    		livroDAO.reduzEstoque(livro.getId());
    	}
    	
    }
    public void calcularMultaSeNecessario(Emprestimo emprestimo) {
        if (LocalDate.now().isAfter(emprestimo.getDataDevolucao())) {
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(emprestimo.getDataDevolucao(), LocalDate.now());
            BigDecimal multa = BigDecimal.valueOf(diasAtraso); // 1 real por dia de atraso
            emprestimo.setMulta(multa);

            // Atualiza a multa no banco de dados
            emprestimoDAO.atualizarMultaNoBanco(emprestimo);

            // Atualiza a multa pendente do usuário
            Usuario usuario = usuarioDAO.listarPorId(emprestimo.getUsuario().getId());
            usuario.setMultaPendente(usuario.getMultaPendente().add(multa));
            usuarioDAO.atualizar(usuario);
        }
    }

    public void marcarEmprestimoComoDevolvido(int emprestimoId) {
        emprestimoDAO.marcarEmprestimoComoDevolvido(emprestimoId);
    }

	public void restaurarEstoqueDosLivros(Emprestimo emprestimo) {
		for (Livro livro : emprestimo.getLivros()) {
	        livroDAO.ajustarEstoque(livro.getId(), 1); // Adiciona 1 de volta ao estoque
	    }
		
	}
}
