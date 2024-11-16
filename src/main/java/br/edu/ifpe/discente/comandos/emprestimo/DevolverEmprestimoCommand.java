package br.edu.ifpe.discente.comandos.emprestimo;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.entity.Emprestimo;
import br.edu.ifpe.discente.domain.repository.EmprestimoDAO;
import br.edu.ifpe.discente.service.EmprestimoService;
import br.edu.ifpe.discente.utils.Constante;

public class DevolverEmprestimoCommand implements Command {

    private EmprestimoService emprestimoService;
    private EmprestimoDAO emprestimoDAO;

	public DevolverEmprestimoCommand(EmprestimoService emprestimoService, EmprestimoDAO emprestimoDAO) {
		super();
		this.emprestimoService = emprestimoService;
		this.emprestimoDAO = emprestimoDAO;
	}

	@Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
    	int emprestimoId = Integer.parseInt(request.getParameter("emprestimoId"));
        Emprestimo emprestimo = emprestimoDAO.listarPorId(emprestimoId);

        //Verifica se o emprestimo existe
        if (emprestimo != null) {
            // Verifica se o empréstimo já foi devolvido
            if (emprestimo.getDataDevolucaoReal() == null) {
                emprestimo.setDataDevolucaoReal(LocalDate.now());

                // Calcula a multa se houver atraso
                emprestimoService.calcularMultaSeNecessario(emprestimo);
                
                // Atualiza o estoque dos livros devolvidos
                emprestimoService.restaurarEstoqueDosLivros(emprestimo);

                // Marca o empréstimo como devolvido no banco de dados
                emprestimoService.marcarEmprestimoComoDevolvido(emprestimoId);
                
            } else {
                throw new RuntimeException("Empréstimo já devolvido.");
            }
        } else {
            throw new RuntimeException("Empréstimo não encontrado.");
        }
        
        return request.getContextPath() + Constante.ACAO_LISTAR_EMPRESTIMOS;
    }
    
}

