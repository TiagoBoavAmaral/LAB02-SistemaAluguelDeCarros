-- Script de inicialização do banco de dados
-- Sistema de Aluguel de Carros

-- Inserir usuários de teste
INSERT INTO users (id, name, email, password, phone, cpf, rg, address, user_type, active, created_at, updated_at) VALUES
(1, 'João Silva', 'cliente@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-1111', '12345678901', '123456789', 'Rua A, 123, São Paulo, SP', 'CLIENT', true, NOW(), NOW()),
(2, 'Maria Santos', 'agente@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-2222', '98765432109', '987654321', 'Av. B, 456, São Paulo, SP', 'AGENT', true, NOW(), NOW()),
(3, 'Carlos Oliveira', 'cliente2@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-3333', '11122233344', '111222333', 'Rua C, 789, São Paulo, SP', 'CLIENT', true, NOW(), NOW()),
(4, 'Ana Costa', 'banco@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-4444', '55566677788', '555666777', 'Av. D, 321, São Paulo, SP', 'AGENT', true, NOW(), NOW());

-- Inserir roles dos usuários
INSERT INTO user_roles (user_id, roles) VALUES
(1, 'ROLE_CLIENT'),
(2, 'ROLE_AGENT'),
(3, 'ROLE_CLIENT'),
(4, 'ROLE_AGENT');

-- Inserir dados específicos de clientes
INSERT INTO clients (id, profession) VALUES
(1, 'Engenheiro de Software'),
(3, 'Médico');

-- Inserir dados específicos de agentes
INSERT INTO agents (id, cnpj, company_name, agent_type) VALUES
(2, '12345678000199', 'RentCar Ltda', 'COMPANY'),
(4, '98765432000188', 'Banco Financeiro S.A.', 'BANK');

-- Inserir empregos para os clientes
INSERT INTO employments (id, client_id, company_name, position, monthly_income, start_date, is_current, created_at, updated_at) VALUES
(1, 1, 'Tech Solutions Ltda', 'Desenvolvedor Senior', 8000.00, '2020-01-15', true, NOW(), NOW()),
(2, 1, 'Freelancer', 'Consultor', 3000.00, '2021-06-01', true, NOW(), NOW()),
(3, 3, 'Hospital São Paulo', 'Cardiologista', 15000.00, '2018-03-01', true, NOW(), NOW()),
(4, 3, 'Clínica Médica ABC', 'Consultor', 5000.00, '2019-08-15', true, NOW(), NOW());

-- Inserir carros
INSERT INTO cars (id, registration, brand, model, year, license_plate, color, daily_rate, status, owner_type, created_at, updated_at) VALUES
(1, 'CAR001', 'Toyota', 'Corolla', 2022, 'ABC-1234', 'Branco', 120.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(2, 'CAR002', 'Honda', 'Civic', 2021, 'DEF-5678', 'Prata', 110.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(3, 'CAR003', 'Volkswagen', 'Jetta', 2023, 'GHI-9012', 'Preto', 140.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(4, 'CAR004', 'Chevrolet', 'Onix', 2022, 'JKL-3456', 'Azul', 90.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(5, 'CAR005', 'Hyundai', 'HB20', 2021, 'MNO-7890', 'Vermelho', 85.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(6, 'CAR006', 'Ford', 'Ka', 2020, 'PQR-1357', 'Branco', 75.00, 'RENTED', 'COMPANY', NOW(), NOW()),
(7, 'CAR007', 'Nissan', 'Versa', 2022, 'STU-2468', 'Prata', 100.00, 'AVAILABLE', 'BANK', NOW(), NOW()),
(8, 'CAR008', 'Renault', 'Logan', 2021, 'VWX-9753', 'Cinza', 95.00, 'MAINTENANCE', 'COMPANY', NOW(), NOW()),
(9, 'CAR009', 'Fiat', 'Argo', 2023, 'YZA-1470', 'Branco', 88.00, 'AVAILABLE', 'COMPANY', NOW(), NOW()),
(10, 'CAR010', 'Peugeot', '208', 2022, 'BCD-2581', 'Azul', 105.00, 'AVAILABLE', 'BANK', NOW(), NOW());

-- Inserir pedidos de aluguel
INSERT INTO rental_orders (id, client_id, car_id, start_date, end_date, total_amount, status, observations, created_at, updated_at) VALUES
(1, 1, 1, '2024-01-15', '2024-01-20', 600.00, 'APPROVED', 'Primeira locação do cliente', NOW(), NOW()),
(2, 3, 2, '2024-01-18', '2024-01-25', 770.00, 'PENDING', 'Viagem de negócios', NOW(), NOW()),
(3, 1, 3, '2024-02-01', '2024-02-05', 560.00, 'UNDER_EVALUATION', 'Viagem de férias', NOW(), NOW()),
(4, 3, 4, '2024-01-10', '2024-01-12', 180.00, 'COMPLETED', 'Locação concluída com sucesso', NOW(), NOW()),
(5, 1, 5, '2024-02-10', '2024-02-15', 425.00, 'CANCELLED', 'Cliente cancelou por motivos pessoais', NOW(), NOW());

-- Inserir avaliações de pedidos
INSERT INTO order_evaluations (id, rental_order_id, agent_id, status, evaluation_notes, evaluated_at) VALUES
(1, 1, 2, 'APPROVED', 'Cliente com boa renda comprovada. Aprovado.', NOW()),
(2, 4, 2, 'APPROVED', 'Cliente médico com excelente histórico financeiro.', NOW()),
(3, 3, 2, 'UNDER_EVALUATION', 'Aguardando documentação adicional do cliente.', NOW());

-- Inserir contratos
INSERT INTO contracts (id, rental_order_id, bank_id, contract_type, signature_date, credit_amount, interest_rate, status, terms, created_at, updated_at) VALUES
(1, 1, NULL, 'RENTAL', NOW(), NULL, NULL, 'ACTIVE', 'Contrato de locação simples. Prazo: 5 dias. Valor total: R$ 600,00.', NOW(), NOW()),
(2, 4, 4, 'RENTAL_WITH_CREDIT', NOW(), 5000.00, 2.5, 'COMPLETED', 'Contrato de locação com crédito bancário. Financiamento aprovado pelo Banco Financeiro S.A.', NOW(), NOW());

-- Inserir dados estatísticos adicionais para demonstração
INSERT INTO cars (id, registration, brand, model, year, license_plate, color, daily_rate, status, owner_type, created_at, updated_at) VALUES
(11, 'CAR011', 'BMW', 'X1', 2023, 'LUX-0001', 'Preto', 250.00, 'AVAILABLE', 'BANK', NOW(), NOW()),
(12, 'CAR012', 'Audi', 'A3', 2022, 'LUX-0002', 'Branco', 220.00, 'AVAILABLE', 'BANK', NOW(), NOW()),
(13, 'CAR013', 'Mercedes', 'A200', 2023, 'LUX-0003', 'Prata', 280.00, 'AVAILABLE', 'BANK', NOW(), NOW()),
(14, 'CAR014', 'Volvo', 'XC40', 2022, 'LUX-0004', 'Azul', 300.00, 'RENTED', 'BANK', NOW(), NOW()),
(15, 'CAR015', 'Land Rover', 'Evoque', 2023, 'LUX-0005', 'Cinza', 350.00, 'AVAILABLE', 'BANK', NOW(), NOW());

-- Inserir mais alguns clientes para demonstração
INSERT INTO users (id, name, email, password, phone, cpf, rg, address, user_type, active, created_at, updated_at) VALUES
(5, 'Pedro Almeida', 'pedro@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-5555', '22233344455', '222333444', 'Rua E, 159, São Paulo, SP', 'CLIENT', true, NOW(), NOW()),
(6, 'Lucia Ferreira', 'lucia@teste.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W', '(11) 99999-6666', '33344455566', '333444555', 'Av. F, 753, São Paulo, SP', 'CLIENT', true, NOW(), NOW());

INSERT INTO user_roles (user_id, roles) VALUES
(5, 'ROLE_CLIENT'),
(6, 'ROLE_CLIENT');

INSERT INTO clients (id, profession) VALUES
(5, 'Advogado'),
(6, 'Arquiteta');

INSERT INTO employments (id, client_id, company_name, position, monthly_income, start_date, is_current, created_at, updated_at) VALUES
(5, 5, 'Escritório Jurídico XYZ', 'Advogado Sênior', 12000.00, '2019-05-01', true, NOW(), NOW()),
(6, 6, 'Arquitetura & Design Ltda', 'Arquiteta', 7500.00, '2020-09-15', true, NOW(), NOW());

-- Inserir mais pedidos para demonstração
INSERT INTO rental_orders (id, client_id, car_id, start_date, end_date, total_amount, status, observations, created_at, updated_at) VALUES
(6, 5, 11, '2024-02-20', '2024-02-25', 1250.00, 'APPROVED', 'Cliente VIP - carro de luxo', NOW(), NOW()),
(7, 6, 7, '2024-02-15', '2024-02-18', 300.00, 'PENDING', 'Primeira locação da cliente', NOW(), NOW()),
(8, 5, 12, '2024-03-01', '2024-03-07', 1320.00, 'UNDER_EVALUATION', 'Viagem de trabalho - uma semana', NOW(), NOW());

-- Inserir mais avaliações
INSERT INTO order_evaluations (id, rental_order_id, agent_id, status, evaluation_notes, evaluated_at) VALUES
(4, 6, 2, 'APPROVED', 'Cliente advogado com excelente renda. Aprovado para carro de luxo.', NOW()),
(5, 7, 2, 'PENDING', 'Primeira locação - aguardando análise de crédito.', NOW());

-- Inserir mais contratos
INSERT INTO contracts (id, rental_order_id, bank_id, contract_type, signature_date, credit_amount, interest_rate, status, terms, created_at, updated_at) VALUES
(3, 6, 4, 'RENTAL_WITH_CREDIT', NOW(), 8000.00, 1.8, 'ACTIVE', 'Contrato de locação premium com crédito pré-aprovado. Cliente VIP.', NOW(), NOW());

-- Comentários sobre as senhas:
-- Todas as senhas dos usuários de teste são: 123456
-- Hash gerado com BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTrukibrcZdu6qfKuJeJpqrpwWTtbJ8W

