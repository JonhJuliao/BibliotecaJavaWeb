package br.edu.ifpe.discente.comandos.usuario;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.edu.ifpe.discente.comandos.Command;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;
import br.edu.ifpe.discente.service.UsuarioService;
import br.edu.ifpe.discente.utils.Constante;

public class PagarMultaUsuarioCommand implements Command {

    private UsuarioDAO usuarioDAO;
    private UsuarioService usuarioService;

    public PagarMultaUsuarioCommand(UsuarioDAO usuarioDAO, UsuarioService usuarioService) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioService = usuarioService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int usuarioId = Integer.parseInt(request.getParameter("id"));
        Usuario usuario = usuarioDAO.listarPorId(usuarioId);

        if (usuario != null && usuario.getMultaPendente().compareTo(BigDecimal.ZERO) > 0) {
            usuarioService.pagarMulta(usuario);
        } else {
            throw new RuntimeException("Usuário não encontrado ou não tem multa pendente.");
        }
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        request.setAttribute("usuarios", usuarios);
        return Constante.LISTA_USUARIOS;
    }
}
