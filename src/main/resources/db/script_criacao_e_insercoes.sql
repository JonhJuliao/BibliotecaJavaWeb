-- Criação do banco de dados
CREATE DATABASE IF NOT EXISTS avarqsoft;
USE avarqsoft;

-- Criação da tabela livro
CREATE TABLE livro (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    anoPublicacao INT NOT NULL,
    quantidade INT NOT NULL DEFAULT 0
);

-- Criação da tabela usuario
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    aniversario DATE NOT NULL,
    multa_pendente DECIMAL(10, 2) DEFAULT 0.00
);

-- Criação da tabela emprestimo
CREATE TABLE emprestimo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT NOT NULL,
    dataAquisicao DATE NOT NULL,
    dataDevolucao DATE,
    multa DECIMAL(10, 2) DEFAULT 0.00,
    dataDevolucaoReal DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

-- Criação da tabela emprestimo_livro
CREATE TABLE emprestimo_livro (
    emprestimo_id INT NOT NULL,
    livro_id INT NOT NULL,
    PRIMARY KEY (emprestimo_id, livro_id),
    FOREIGN KEY (emprestimo_id) REFERENCES emprestimo(id),
    FOREIGN KEY (livro_id) REFERENCES livro(id)
);

-- Inserções para teste
-- Inserção de livros
INSERT INTO livro (titulo, autor, anoPublicacao, quantidade) VALUES 
('Dom Casmurro', 'Machado de Assis', 1899, 3),
('O Senhor dos Anéis', 'J.R.R. Tolkien', 1954, 5),
('1984', 'George Orwell', 1949, 2),
('A Revolução dos Bichos', 'George Orwell', 1945, 4),
('Moby Dick', 'Herman Melville', 1851, 1);

-- Inserção de usuários
INSERT INTO usuario (nome, aniversario, multa_pendente) VALUES 
('João Silva', '1990-05-15', 0.00),
('Maria Oliveira', '1985-10-22', 5.00),
('Carlos Pereira', '2000-03-30', 0.00),
('Ana Souza', '1995-07-19', 2.00);

-- Inserção de empréstimos
INSERT INTO emprestimo (usuario_id, dataAquisicao, dataDevolucao, multa, dataDevolucaoReal) VALUES 
(1, '2024-10-01', '2024-10-08', 0.00, NULL),
(2, '2024-09-15', '2024-09-22', 5.00, '2024-09-25'),
(3, '2024-11-01', '2024-11-08', 0.00, NULL);

-- Inserção de relação empréstimo-livro
INSERT INTO emprestimo_livro (emprestimo_id, livro_id) VALUES 
(1, 1),
(1, 3),
(2, 2),
(2, 4),
(3, 5);
