<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="br.edu.ifpe.discente.domain.entity.Usuario" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Lista de Usuarios Cadastrados</title>
</head>
<body>
	<table border=1>
		<%
		List<Usuario> usuarios = (ArrayList<Usuario>) request.getAttribute("usuarios");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		%>
		<thead>
			<tr>
				<td>Nome</td>
				<td>Aniversario</td>
				<td>Multa pendente</td>
				<td colspan="3">Ação</td>
			</tr>
		</thead>
		<tbody>
			<%
			for (Usuario usuario: usuarios){
			%>
			<tr>
				<td><%=usuario.getNome()%></td>
				<td><%=usuario.getAniversario().format(fmt)%></td>
				<td><%=usuario.getMultaPendente() %>
				<td><a href="/AVArqSoft/app?acao=editarUsuario&id=<%=usuario.getId()%>">Editar</a></td>
				<td><a href="/AVArqSoft/app?acao=removerUsuario&id=<%=usuario.getId()%>">Remover</a></td>
				<td>
                    <% if (usuario.getMultaPendente().compareTo(BigDecimal.ZERO) > 0) { %>
                        <a href="/AVArqSoft/app?acao=pagarMultaUsuario&id=<%=usuario.getId()%>">Pagar Multa</a>
                    <% } %>
                </td>
			</tr>
			<%
			}
			%>
		</tbody>
		
	</table>
	<a href="/AVArqSoft/app?acao=novoUsuario">Adicionar usuário</a>
</body>
</html>