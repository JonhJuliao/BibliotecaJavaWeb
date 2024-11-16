<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="br.edu.ifpe.discente.domain.entity.Usuario" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Controle de Usuários</title>
</head>
<body>
	<h1>Controle de Usuários</h1>
	<hr>
	<% Usuario usuario = (Usuario) request.getAttribute("usuario"); %>
	<form action="app" method="POST">
		<input type="hidden" name="acao" value=<%=(usuario!= null ? "atualizarUsuario" : "cadastrarUsuario") %>>
		<input type="hidden" name="id" value=<%=(usuario!=null ? usuario.getId() : "") %>>
		<div>
			<label for="nome">Nome</label> 
			<input name = "nome" id = "nome" value="<%=(usuario!=null ? usuario.getNome() : "") %>">
		</div>
		<div>
			<label for="aniversario">Data de aniversário</label> 
			<input type="date" name = "aniversario" id = "aniversario" value="<%=(usuario!=null ? usuario.getAniversario() : "") %>">
		</div>
		<button>Enviar</button>
	</form>
</body>
</html>