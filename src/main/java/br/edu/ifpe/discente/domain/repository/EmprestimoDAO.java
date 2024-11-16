package br.edu.ifpe.discente.domain.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpe.discente.domain.entity.Emprestimo;
import br.edu.ifpe.discente.domain.entity.Livro;
import br.edu.ifpe.discente.domain.entity.Usuario;
import br.edu.ifpe.discente.infra.db.DBConnection;
import br.edu.ifpe.discente.utils.SQLUtils;

public class EmprestimoDAO implements DAO<Emprestimo> {
	
	private DBConnection connection;
	private LivroDAO livroDAO;
	private UsuarioDAO usuarioDAO;

	public EmprestimoDAO(DBConnection connection, LivroDAO livroDAO, UsuarioDAO usuarioDAO) {
		super();
		this.connection = connection;
		this.livroDAO = livroDAO;
		this.usuarioDAO = usuarioDAO;
	}

	@Override
    public void salvar(Emprestimo emprestimo) {
        Connection con = this.connection.getConnection();
        try {
            String sqlInsert = SQLUtils.getInsertSQL("emprestimo", "usuario_id", "dataAquisicao", "dataDevolucao");
            PreparedStatement pstm = con.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, emprestimo.getUsuario().getId());
            pstm.setDate(2, java.sql.Date.valueOf(emprestimo.getDataAquisicao()));
            pstm.setDate(3, java.sql.Date.valueOf(emprestimo.getDataDevolucao()));
            pstm.executeUpdate();

            // Obter o ID gerado para associar livros ao empréstimo
            ResultSet rsKeys = pstm.getGeneratedKeys();
            if (rsKeys.next()) {
                int emprestimoId = rsKeys.getInt(1);
                emprestimo.setId(emprestimoId);

                // Inserir a relação entre empréstimo e livros
                String sqlInsertLivroEmprestimo = SQLUtils.getInsertSQL("emprestimo_livro", "emprestimo_id", "livro_id");
                for (Livro livro : emprestimo.getLivros()) {
                    PreparedStatement pstmLivro = con.prepareStatement(sqlInsertLivroEmprestimo);
                    pstmLivro.setInt(1, emprestimoId);
                    pstmLivro.setInt(2, livro.getId());
                    pstmLivro.executeUpdate();
                    pstmLivro.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
    }

    @Override
    public List<Emprestimo> listarTodos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        Connection con = this.connection.getConnection();
        try {
            String sqlSelect = SQLUtils.getSelectSQL("emprestimo");
            PreparedStatement pstm = con.prepareStatement(sqlSelect);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Emprestimo emprestimo = new Emprestimo();
                emprestimo.setId(rs.getInt("id"));
                Usuario usuario =  usuarioDAO.listarPorId(rs.getInt("usuario_id"));
                usuario.setId(rs.getInt("usuario_id"));
                emprestimo.setUsuario(usuario);
                emprestimo.setDataAquisicao(rs.getDate("dataAquisicao").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
                emprestimo.setDataDevolucaoReal(rs.getDate("dataDevolucaoReal") != null ? rs.getDate("dataDevolucaoReal").toLocalDate() : null);

                // Buscar e adicionar livros associados ao empréstimo
                List<Livro> livros = buscarLivrosPorEmprestimoId(emprestimo.getId(), con);
                emprestimo.setLivros(livros);
                
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return emprestimos;
    }

    @Override
    public void remover(int id) {
        Connection con = this.connection.getConnection();
        try {
        	// Verificar se o empréstimo foi devolvido antes de remover
            Emprestimo emprestimo = listarPorId(id);
            if (emprestimo == null) {
                throw new RuntimeException("Empréstimo não encontrado.");
            }
            if (emprestimo.getDataDevolucaoReal() == null) {
                throw new RuntimeException("Não é permitido remover um empréstimo que ainda não foi devolvido.");
            }

            // Remover as relações com livros primeiro
            String sqlDeleteLivros = SQLUtils.getDeleteSQL("emprestimo_livro", "emprestimo_id");
            PreparedStatement pstmLivros = con.prepareStatement(sqlDeleteLivros);
            pstmLivros.setInt(1, id);
            pstmLivros.executeUpdate();
            pstmLivros.close();

            // Remover o empréstimo
            String sqlDelete = SQLUtils.getDeleteSQL("emprestimo", "id");
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
    public Emprestimo atualizar(Emprestimo emprestimo) {
        Connection con = this.connection.getConnection();
        try {
        	if (emprestimo.getDataDevolucaoReal() != null) {
                throw new RuntimeException("Não é permitido editar um empréstimo que foi devolvido.");
            }
            String sqlUpdate = SQLUtils.getUpdateSQL("emprestimo", new String[]{"usuario_id", "dataAquisicao", "dataDevolucao"}, "id");
            PreparedStatement pstm = con.prepareStatement(sqlUpdate);
            pstm.setInt(1, emprestimo.getUsuario().getId());
            pstm.setDate(2, java.sql.Date.valueOf(emprestimo.getDataAquisicao()));
            pstm.setDate(3, java.sql.Date.valueOf(emprestimo.getDataDevolucao()));
            pstm.setInt(4, emprestimo.getId());
            pstm.executeUpdate();
            
            // Remove os livros antigos associados ao empréstimo
            String sqlDeleteLivros = "DELETE FROM emprestimo_livro WHERE emprestimo_id = ?";
            PreparedStatement pstmDeleteLivros = con.prepareStatement(sqlDeleteLivros);
            pstmDeleteLivros.setInt(1, emprestimo.getId());
            pstmDeleteLivros.executeUpdate();

            // Adiciona os livros atualizados associados ao empréstimo
            String sqlInsertLivros = "INSERT INTO emprestimo_livro (emprestimo_id, livro_id) VALUES (?, ?)";
            for (Livro livro : emprestimo.getLivros()) {
                PreparedStatement pstmInsertLivro = con.prepareStatement(sqlInsertLivros);
                pstmInsertLivro.setInt(1, emprestimo.getId());
                pstmInsertLivro.setInt(2, livro.getId());
                pstmInsertLivro.executeUpdate();
                pstmInsertLivro.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return emprestimo;
    }

    @Override
    public Emprestimo listarPorId(int id) {
        Emprestimo emprestimo = null;
        Connection con = this.connection.getConnection();
        try {
            String sqlSelectById = SQLUtils.getSelectSQLById("emprestimo", "id");
            PreparedStatement pstm = con.prepareStatement(sqlSelectById);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                emprestimo = new Emprestimo();
                emprestimo.setId(rs.getInt("id"));
                Usuario usuario = new Usuario(); 
                usuario.setId(rs.getInt("usuario_id"));
                emprestimo.setUsuario(usuario);
                emprestimo.setDataAquisicao(rs.getDate("dataAquisicao").toLocalDate());
                emprestimo.setDataDevolucao(rs.getDate("dataDevolucao").toLocalDate());
                emprestimo.setDataDevolucaoReal(rs.getDate("dataDevolucaoReal") != null ? rs.getDate("dataDevolucaoReal").toLocalDate() : null);

                // Buscar e adicionar livros associados ao empréstimo
                List<Livro> livros = buscarLivrosPorEmprestimoId(emprestimo.getId(), con);
                emprestimo.setLivros(livros);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.connection.close(con);
        }
        return emprestimo;
    }

	private List<Livro> buscarLivrosPorEmprestimoId(int emprestimoId, Connection con) throws SQLException {
	    List<Livro> livros = new ArrayList<>();
	    String sqlSelectLivros = "SELECT livro_id FROM emprestimo_livro WHERE emprestimo_id = ?";
	    PreparedStatement pstmLivros = con.prepareStatement(sqlSelectLivros);
	    pstmLivros.setInt(1, emprestimoId);
	    ResultSet rsLivros = pstmLivros.executeQuery();
	    while (rsLivros.next()) {
	    	int livroId = rsLivros.getInt("livro_id");
	        Livro livro = livroDAO.listarPorId(livroId);
	        if (livro != null) {
	            livros.add(livro);
	        }
	    }
	    pstmLivros.close();
	    return livros;
	}

	public void atualizarMultaNoBanco(Emprestimo emprestimo) {
		Connection con = this.connection.getConnection();
	    try {
	        String sqlUpdate = SQLUtils.getUpdateSQL("emprestimo", new String[]{"multa"}, "id"); 
	        PreparedStatement pstm = con.prepareStatement(sqlUpdate);
	        pstm.setBigDecimal(1, emprestimo.getMulta());
	        pstm.setInt(2, emprestimo.getId());
	        pstm.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.connection.close(con);
	    }
	}

	public boolean verificarEmprestimoAtivo(int usuarioId) {
		Connection con = this.connection.getConnection();
	    boolean emprestimoAtivo = false;

	    try {
	        // SQL para verificar se existe um empréstimo ativo (não devolvido) para o usuário
	        String sqlSelect = "SELECT COUNT(*) FROM emprestimo WHERE usuario_id = ? AND dataDevolucaoReal IS NULL";
	        PreparedStatement pstm = con.prepareStatement(sqlSelect);
	        pstm.setInt(1, usuarioId);

	        ResultSet rs = pstm.executeQuery();
	        if (rs.next()) {
	            emprestimoAtivo = rs.getInt(1) > 0; // Retorna true se o usuário tiver pelo menos um empréstimo ativo
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.connection.close(con);
	    }
		return emprestimoAtivo;
	}
	
	public void marcarEmprestimoComoDevolvido(int emprestimoId) {
	    Connection con = this.connection.getConnection();
	    try {
	        // Atualiza o empréstimo para definir a data de devolução como a data atual
	        String sqlUpdate = "UPDATE emprestimo SET dataDevolucaoReal = ? WHERE id = ?";
	        PreparedStatement pstm = con.prepareStatement(sqlUpdate);
	        pstm.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Marca a data atual como a data de devolução real
	        pstm.setInt(2, emprestimoId);
	        pstm.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        this.connection.close(con);
	    }
	}
	

}
