package br.edu.ifpe.discente.domain.repository;

import java.util.List;

public interface DAO<T> {
	
	void salvar(T t);
	List<T> listarTodos();
	void remover(int id);
	T atualizar(T t);
	T listarPorId(int id);

}
