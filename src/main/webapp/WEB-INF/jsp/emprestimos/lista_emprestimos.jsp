<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="br.edu.ifpe.discente.domain.entity.Emprestimo" %>
<%@ page import="br.edu.ifpe.discente.domain.entity.Livro" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Lista de Emprestimos</title>
</head>
<body>
	<script>
		function confirmarDevolucao() {
			return confirm("Tem certeza de que deseja devolver este empréstimo?");
		}
	</script>
	<h1>Lista de Empréstimos Cadastrados</h1>
    <table border="1">
        <%
        List<Emprestimo> emprestimos = (ArrayList<Emprestimo>) request.getAttribute("emprestimos");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        %>
        <thead>
            <tr>
                <td>Usuário</td>
                <td>Livros</td>
                <td>Data de Aquisição</td>
                <td>Data de Devolução</td>
                <td>Data de Devolução Real</td>
                <td colspan="3">Ação</td>
            </tr>
        </thead>
        <tbody>
            <%
            for (Emprestimo emprestimo : emprestimos) {
                %>
                <tr>
                    <td><%= emprestimo.getUsuario().getNome() %></td>
                    <td>
                        <ul>
                            <%
                            for (Livro livro : emprestimo.getLivros()) {
                                %>
                                <li><%= livro.getTitulo() %></li>
                                <%
                            }
                            %>
                        </ul>
                    </td>
                    <td><%= emprestimo.getDataAquisicao().format(fmt) %></td>
                    <td><%= emprestimo.getDataDevolucao().format(fmt) %></td>
                    <td><%= (emprestimo.getDataDevolucaoReal() != null) ? emprestimo.getDataDevolucaoReal().format(fmt) : "Ainda não devolvido" %></td>
                    <td><a href="/AVArqSoft/app?acao=editarEmprestimo&id=<%= emprestimo.getId() %>">Editar</a></td>
                    <td><a href="/AVArqSoft/app?acao=removerEmprestimo&id=<%= emprestimo.getId() %>">Remover</a></td>
                    <td>
					    <form action="/AVArqSoft/app" method="POST" onsubmit="return confirmarDevolucao();">
					        <input type="hidden" name="acao" value="devolverEmprestimo">
					        <input type="hidden" name="emprestimoId" value="<%= emprestimo.getId() %>">
					        <button type="submit">Devolver</button>
					    </form>
					</td>
					
                </tr>
                <%
			}
			%>
        </tbody>
    </table>
    <a href="/AVArqSoft/app?acao=novoEmprestimo">Adicionar emprestimo</a>
</body>
</html>