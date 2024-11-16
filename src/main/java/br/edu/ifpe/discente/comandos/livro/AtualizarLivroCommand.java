package br.edu.ifpe.discente.comandos.livro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.domain.repository.LivroDAO;
import br.edu.ifpe.discente.utils.Constante;

public class AtualizarLivroCommand implements Command {
	
	private LivroDAO dao;

	public AtualizarLivroCommand(LivroDAO dao) {
		super();
		this.dao = dao;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		var livro = new Livro(request.getParameter("titulo"),request.getParameter("autor"), Integer.parseInt(request.getParameter("anoPublicacao")),Integer.parseInt(request.getParameter("quantidade")));
		livro.setId(Integer.parseInt(request.getParameter("id")));
		this.dao.atualizar(livro);
		return request.getContextPath() + Constante.ACAO_LISTAR_lIVROS;
	}

}
