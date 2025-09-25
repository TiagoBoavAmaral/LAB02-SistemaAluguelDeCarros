-- Senha para todos: 'password' (BCrypt encoded)
-- Inserir Cliente
INSERT INTO users (id, email, password, role) VALUES (1, 'cliente@email.com', '$2a$10$I4lD2pfnimOEsoOdI.xJxeizuBvRi1dtTkDu5buJWZxXCWyds8Dp.', 'ROLE_CLIENTE');
INSERT INTO cliente (id, nome, rg, cpf, endereco, profissao) VALUES (1, 'Joao Cliente', 'MG-123456', '123.456.789-00', 'Rua A, 123', 'Engenheiro');

-- Inserir Agente
INSERT INTO users (id, email, password, role) VALUES (2, 'agente@email.com', '$2a$10$I4lD2pfnimOEsoOdI.xJxeizuBvRi1dtTkDu5buJWZxXCWyds8Dp.', 'ROLE_AGENTE');
INSERT INTO agente (id, nome_empresa) VALUES (2, 'Banco Fictício S/A');

-- Inserir Automóveis
INSERT INTO automovel (id, matricula, ano, marca, modelo, placa) VALUES (101, 'MAT01', 2023, 'Fiat', 'Mobi', 'ABC-1234');
INSERT INTO automovel (id, matricula, ano, marca, modelo, placa) VALUES (102, 'MAT02', 2024, 'Chevrolet', 'Onix', 'DEF-5678');
