package br.edu.ifpe.discente.service;

import java.math.BigDecimal;

import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.domain.repository.UsuarioDAO;

public class UsuarioService {
    
    private UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void pagarMulta(Usuario usuario) {
        if (usuario.getMultaPendente().compareTo(BigDecimal.ZERO) > 0) {
            usuario.setMultaPendente(BigDecimal.ZERO);
            usuarioDAO.atualizar(usuario);
        } else {
            throw new RuntimeException("Usuário não tem multa pendente.");
        }
    }
}

