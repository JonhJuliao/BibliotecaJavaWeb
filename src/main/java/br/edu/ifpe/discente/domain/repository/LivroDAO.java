package br.edu.ifpe.discente.domain.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.infra.db.DBConnection;
import br.edu.ifpe.discente.utils.SQLUtils;

public class LivroDAO implements DAO<Livro> {
	
	private DBConnection connection;
	
	public LivroDAO(DBConnection connection) {
		this.connection = connection;
	}

	@Override
	public void salvar(Livro livro) {
		
		Connection con = this.connection.getConnection();
		
		try {
			String sqlInsert = SQLUtils.getInsertSQL("livro", "titulo", "autor", "anoPublicacao", "quantidade");
			PreparedStatement pstm = con.prepareStatement(sqlInsert);
			pstm.setString(1, livro.getTitulo());
			pstm.setString(2, livro.getAutor());
			pstm.setInt(3, livro.getAnoPublicacao());
			pstm.setInt(4, livro.getQuantidade());
//			String queryLog = sqlInsert.replaceFirst("\\?", "'" + livro.getTitulo() + "'")
//                    .replaceFirst("\\?", "'" + livro.getAutor() + "'")
//                    .replaceFirst("\\?", String.valueOf(livro.getAnoPublicacao()))
//                    .replaceFirst("\\?", String.valueOf(livro.getQuantidade()));
//
//			System.out.println("Query para execução: " + queryLog);
			pstm.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.connection.close(con);
		}
	}

	@Override
	public List<Livro> listarTodos() {
		List<Livro> livros = new ArrayList<>();
		Connection con = this.connection.getConnection();
		
		try {
			String sqlSelect = SQLUtils.getSelectSQL("livro");
			PreparedStatement pstm = con.prepareStatement(sqlSelect);
			ResultSet rs = pstm.executeQuery();
			while(rs.next()) {
				var livro = new Livro(rs.getString("titulo"), rs.getString("autor"), rs.getInt("anoPublicacao"), rs.getInt("quantidade"));
				livro.setId(rs.getInt("id"));
				livros.add(livro);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.connection.close(con);
		}
		return livros;
	}

	@Override
	public void remover(int id) {
		Connection con = this.connection.getConnection();
		try {
			String sqlDelete = SQLUtils.getDeleteSQL("livro", "id");
			PreparedStatement pstm = con.prepareStatement(sqlDelete);
			pstm.setInt(1, id);
			pstm.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.connection.close(con);
		}
		
	}

	@Override
	public Livro atualizar(Livro livro) {
		Connection con = this.connection.getConnection();
		try {
			String sqlUpdate = SQLUtils.getUpdateSQL("livro", new String[]{"titulo","autor","anoPublicacao", "quantidade"}, "id");
			PreparedStatement pstm = con.prepareStatement(sqlUpdate);
			pstm.setString(1, livro.getTitulo());
			pstm.setString(2, livro.getAutor());
			pstm.setInt(3, livro.getAnoPublicacao());
			pstm.setInt(4, livro.getQuantidade());
			pstm.setInt(5, livro.getId());
			pstm.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.connection.close(con);
		}
		return livro;
	}

	@Override
	public Livro listarPorId(int id) {
		Livro livro = null;
		Connection con = this.connection.getConnection();
		try {
			String sqlSelectById = SQLUtils.getSelectSQLById("livro", "id");
			PreparedStatement pstm = con.prepareStatement(sqlSelectById);
			pstm.setInt(1, id);
			ResultSet rs =pstm.executeQuery();
			if(rs.next()) {
				livro = new Livro(rs.getString("titulo"), rs.getString("autor"), rs.getInt("anoPublicacao"),rs.getInt("quantidade"));
				livro.setId(rs.getInt("id"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			this.connection.close(con);
		}
		return livro;
	}
	
	public void reduzEstoque(int livroId) {
	    Connection con = this.connection.getConnection();
	    try {
	        String sqlUpdateEstoque = "UPDATE livro SET quantidade = quantidade - 1 WHERE id = ?";
	        PreparedStatement pstm = con.prepareStatement(sqlUpdateEstoque);
	        pstm.setInt(1, livroId);
	        pstm.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.connection.close(con);
	    }
	}

	public void ajustarEstoque(int livroId, int diferenca) {
		Connection con = this.connection.getConnection();
	    try {
	        String sqlUpdateEstoque = "UPDATE livro SET quantidade = quantidade + ? WHERE id = ?";
	        PreparedStatement pstm = con.prepareStatement(sqlUpdateEstoque);
	        pstm.setInt(1, diferenca);
	        pstm.setInt(2, livroId);
	        pstm.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.connection.close(con);
	    }
		
	}

}
