<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="br.edu.ifpe.discente.domain.entity.Livro" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Lista de Livros</title>
</head>
<body>
	<table border=1>
		<%
		List<Livro> livros = (ArrayList<Livro>) request.getAttribute("livros");
		%>
		<thead>
			<tr>
				<td>Título</td>
				<td>Autor</td>
				<td>Ano de Publicação</td>
				<td>Quantidade</td>
				<td colspan="2">Ação</td>
			</tr>
		</thead>
		<tbody>
			<%
			for (Livro livro: livros){
			%>
			<tr>
				<td><%=livro.getTitulo()%></td>
				<td><%=livro.getAutor()%></td>
				<td><%=livro.getAnoPublicacao()%></td>
				<td><%=livro.getQuantidade()%></td>
				<td><a href="/AVArqSoft/app?acao=editarLivro&id=<%=livro.getId()%>">Editar</a></td>
				<td><a href="/AVArqSoft/app?acao=removerLivro&id=<%=livro.getId()%>">Remover</a></td>
			</tr>
			<%
			}
			%>
		</tbody>
		
	</table>
	<a href="/AVArqSoft/app?acao=novoLivro">Adicionar livro</a>
</body>
</html>