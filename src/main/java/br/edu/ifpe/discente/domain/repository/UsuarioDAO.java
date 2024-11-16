package br.edu.ifpe.discente.domain.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.infra.db.DBConnection;
import br.edu.ifpe.discente.utils.SQLUtils;

public class UsuarioDAO implements DAO<Usuario> {

    private DBConnection connection;

    public UsuarioDAO(DBConnection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Usuario usuario) {
        Connection con = this.connection.getConnection();
        try {
            String sqlInsert = SQLUtils.getInsertSQL("usuario", "nome", "aniversario");
            PreparedStatement pstm = con.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setString(1, usuario.getNome());
            pstm.setDate(2, java.sql.Date.valueOf(usuario.getAniversario()));
            pstm.executeUpdate();

            ResultSet rsKeys = pstm.getGeneratedKeys();
            if (rsKeys.next()) {
                usuario.setId(rsKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = this.connection.getConnection();
        try {
            String sqlSelect = SQLUtils.getSelectSQL("usuario");
            PreparedStatement pstm = con.prepareStatement(sqlSelect);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
            	Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setAniversario(rs.getDate("aniversario").toLocalDate());
                usuario.setMultaPendente(rs.getBigDecimal("multa_pendente"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return usuarios;
    }

    @Override
    public void remover(int id) {
        Connection con = this.connection.getConnection();
        try {
            String sqlDelete = SQLUtils.getDeleteSQL("usuario", "id");
            PreparedStatement pstm = con.prepareStatement(sqlDelete);
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        Connection con = this.connection.getConnection();
        try {
            String sqlUpdate = SQLUtils.getUpdateSQL("usuario", new String[]{"nome", "aniversario", "multa_pendente"}, "id");
            PreparedStatement pstm = con.prepareStatement(sqlUpdate);
            pstm.setString(1, usuario.getNome());
            pstm.setDate(2, java.sql.Date.valueOf(usuario.getAniversario()));
            pstm.setBigDecimal(3, usuario.getMultaPendente());
            pstm.setInt(4, usuario.getId());
            pstm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return usuario;
    }

    @Override
    public Usuario listarPorId(int id) {
        Usuario usuario = null;
        Connection con = this.connection.getConnection();
        try {
            String sqlSelectById = SQLUtils.getSelectSQLById("usuario", "id");
            PreparedStatement pstm = con.prepareStatement(sqlSelectById);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
            	usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setAniversario(rs.getDate("aniversario").toLocalDate()); 
                usuario.setMultaPendente(rs.getBigDecimal("multa_pendente"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return usuario;
    }
}
