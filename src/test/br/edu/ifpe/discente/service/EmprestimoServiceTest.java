package br.edu.ifpe.discente.service;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.edu.ifpe.discente.domain.entity.Emprestimo;
import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.service.EmprestimoService;

public class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;
    private EmprestimoDAO emprestimoDAO;
    private LivroDAO livroDAO;
    private UsuarioDAO usuarioDAO;

    @Before
    public void setUp() {
        emprestimoDAO = mock(EmprestimoDAO.class);
        livroDAO = mock(LivroDAO.class);
        usuarioDAO = mock(UsuarioDAO.class);
        emprestimoService = new EmprestimoService(emprestimoDAO, livroDAO, usuarioDAO);
    }

    @Test
    public void testVerificaMultaPendente() {
        // Cenário: Verificar se um usuário com multa pendente não pode fazer um novo empréstimo.
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setMultaPendente(BigDecimal.TEN);

        try {
            emprestimoService.verificaMultaPendente(usuario);
            fail("Deveria ter lançado uma exceção");
        } catch (RuntimeException e) {
            assertEquals("O usuário tem multa pendente e portanto não pode realizar um novo empréstimo!", e.getMessage());
        }
    }

    @Test
    public void testValidarQuantidadeDeLivros() {
        // Cenário: Verificar se um empréstimo com mais de 3 livros é impedido.
        List<Livro> livros = Arrays.asList(new Livro(), new Livro(), new Livro(), new Livro());

        try {
            emprestimoService.validarQuantidadeDeLivros(livros);
            fail("Deveria ter lançado uma exceção");
        } catch (RuntimeException e) {
            assertEquals("Não é permitido pegar mais do que 3 livros emprestados de uma vez!", e.getMessage());
        }
    }

    @Test
    public void testValidarLivrosUnicos() {
        // Cenário: Verificar se um empréstimo contendo mais de uma cópia do mesmo livro é impedido.
        Livro livro1 = new Livro();
        livro1.setId(1);
        Livro livro2 = new Livro();
        livro2.setId(1); // Mesmo ID para simular duplicidade

        List<Livro> livros = Arrays.asList(livro1, livro2);

        try {
            emprestimoService.validarLivrosUnicos(livros);
            fail("Deveria ter lançado uma exceção");
        } catch (RuntimeException e) {
            assertEquals("Não é permitido levar mais de uma cópia do mesmo livro!", e.getMessage());
        }
    }

    @Test
    public void testValidarESalvarEmprestimoComEstoqueEsgotado() {
        // Cenário: Verificar se um empréstimo é impedido quando o estoque de um livro está esgotado.
        Livro livro1 = new Livro();
        livro1.setId(1);
        livro1.setTitulo("Livro Teste");
        livro1.setQuantidade(0); // Sem estoque

        when(livroDAO.listarPorId(1)).thenReturn(livro1);

        List<Livro> livros = Arrays.asList(livro1);

        try {
            emprestimoService.validarESalvarEmprestimo(new Emprestimo(null, livros));
            fail("Deveria ter lançado uma exceção");
        } catch (RuntimeException e) {
            assertEquals("O livro 'Livro Teste' não possui estoque disponível.", e.getMessage());
        }
    }

    @Test
    public void testCalcularMultaSeNecessario() {
        // Cenário: Verificar se a multa é calculada corretamente quando o empréstimo está em atraso.
        Emprestimo emprestimo = new Emprestimo();
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setMultaPendente(BigDecimal.ZERO);

        emprestimo.setUsuario(usuario);
        emprestimo.setDataDevolucao(LocalDate.now().minusDays(5)); // Atrasado 5 dias

        when(usuarioDAO.listarPorId(1)).thenReturn(usuario);

        emprestimoService.calcularMultaSeNecessario(emprestimo);

        assertEquals(BigDecimal.valueOf(5), emprestimo.getMulta());
        verify(usuarioDAO).atualizar(usuario);
    }
}
