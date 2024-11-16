<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="br.edu.ifpe.discente.domain.entity.Emprestimo" %>
<%@ page language="java" import="br.edu.ifpe.discente.domain.entity.Usuario" %>
<%@ page language="java" import="br.edu.ifpe.discente.domain.entity.Livro" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Controle de Empréstimos</title>
</head>
<body>
    <h1>Controle de Empréstimos</h1>
    <hr>
    <%
        Emprestimo emprestimo = (Emprestimo) request.getAttribute("emprestimo");
        List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
        List<Livro> livros = (List<Livro>) request.getAttribute("livros");
    %>
    <form action="app" method="POST">
        <input type="hidden" name="acao" value="<%=(emprestimo != null ? "atualizarEmprestimo" : "cadastrarEmprestimo") %>">
        <input type="hidden" name="emprestimoId" value="<%=(emprestimo != null ? emprestimo.getId() : "") %>">
        
        <div>
            <label for="usuario">Usuário</label>
            <select name="usuarioId" id="usuario">
                <option value="">Selecione um usuário</option>
                <%
                    for (Usuario usuario : usuarios) {
                        String selected = (emprestimo != null && emprestimo.getUsuario().getId() == usuario.getId()) ? "selected" : "";
                %>
                <option value="<%= usuario.getId() %>" <%= selected %>><%= usuario.getNome() %></option>
                <%
                    }
                %>
            </select>
        </div>

        <div>
            <label for="livros">Livros</label>
            <select name="livroIds" id="livros" multiple>
                <%
                    for (Livro livro : livros) {
                        boolean isSelected = (emprestimo != null && emprestimo.getLivros().stream().anyMatch(l -> l.getId() == livro.getId()));
                        String selected = isSelected ? "selected" : "";
                %>
                <option value="<%= livro.getId() %>" <%= selected %>><%= livro.getTitulo() %></option>
                <%
                    }
                %>
            </select>
            <p>Segure CTRL (ou CMD no Mac) para selecionar múltiplos livros (Min: 1, Max: 3)</p>
        </div>

        <button>Enviar</button>
    </form>
</body>
</html>
