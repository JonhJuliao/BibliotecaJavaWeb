package br.edu.ifpe.discente.infra.db;

import java.sql.Connection;

public interface DBConnection {

	Connection getConnection();
	void close(Connection connection);
}
