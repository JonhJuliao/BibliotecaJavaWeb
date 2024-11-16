<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="br.edu.ifpe.discente.domain.entity.Livro" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Controle de Livros</title>
</head>
<body>
	<h1>Controle de Livros</h1>
	<hr>
	<% Livro livro = (Livro) request.getAttribute("livro"); %>
	<form action="app" method="POST">
		<input type="hidden" name="acao" value=<%=(livro!= null ? "atualizarLivro" : "cadastrarLivro") %>>
		<input type="hidden" name="id" value=<%=(livro!=null ? livro.getId() : "") %>>
		<div>
			<label for="titulo">Titulo</label> 
			<input name = "titulo" id = "titulo" value="<%=(livro!=null ? livro.getTitulo() : "") %>">
		</div>
		<div>
			<label for="autor">Autor</label> 
			<input name = "autor" id = "autor" value="<%=(livro!=null ? livro.getAutor() : "") %>">
		</div>
		<div>
			<label for="anoPublicacao">Ano de Publicação</label> 
			<input name = "anoPublicacao" id = "anoPublicacao" value=<%=(livro!=null ? livro.getAnoPublicacao() : "") %>>
		</div>
		<div>
			<label for="quantidade">Quantidade</label> 
			<input name = "quantidade" id = "quantidade" value=<%=(livro!=null ? livro.getQuantidade() : "") %>>
		</div>
		<button>Enviar</button>
	</form>
</body>
</html>