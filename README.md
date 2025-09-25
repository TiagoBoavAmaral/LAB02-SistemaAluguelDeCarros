# Sistema de Aluguel de Carros

Sistema web desenvolvido em Java com Spring Boot para gerenciamento de aluguel de carros, implementando arquitetura MVC com autenticação, autorização e regras de negócio completas.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura](#arquitetura)
- [Instalação e Execução](#instalação-e-execução)
- [Configuração](#configuração)
- [Uso do Sistema](#uso-do-sistema)
- [API Endpoints](#api-endpoints)
- [Banco de Dados](#banco-de-dados)
- [Segurança](#segurança)
- [Testes](#testes)
- [Contribuição](#contribuição)
- [Licença](#licença)

## 🎯 Visão Geral

O Sistema de Aluguel de Carros é uma aplicação web completa que permite o gerenciamento de locação de veículos, com diferentes tipos de usuários (clientes e agentes), processo de aprovação financeira e integração com contratos de crédito bancário.

### Principais Características

- **Cadastro de Usuários**: Sistema de registro para clientes individuais e agentes (empresas/bancos)
- **Gestão de Veículos**: Cadastro e controle de automóveis com diferentes status
- **Pedidos de Aluguel**: Criação, avaliação e aprovação de solicitações de locação
- **Contratos**: Geração automática de contratos simples ou com crédito bancário
- **Segurança**: Autenticação e autorização baseada em roles
- **Interface Responsiva**: Frontend moderno com Bootstrap e Thymeleaf

## ✨ Funcionalidades

### Para Clientes
- ✅ Cadastro com informações pessoais e profissionais
- ✅ Gerenciamento de até 3 empregos/rendimentos
- ✅ Visualização de carros disponíveis
- ✅ Criação de pedidos de aluguel
- ✅ Acompanhamento do status dos pedidos
- ✅ Cancelamento de pedidos (quando permitido)
- ✅ Dashboard personalizado

### Para Agentes (Empresas/Bancos)
- ✅ Cadastro com CNPJ e informações corporativas
- ✅ Gerenciamento completo de clientes
- ✅ Cadastro e controle de veículos
- ✅ Avaliação financeira de pedidos
- ✅ Aprovação/rejeição de solicitações
- ✅ Criação de contratos
- ✅ Gestão de contratos de crédito (bancos)

### Funcionalidades do Sistema
- ✅ Autenticação segura com Spring Security
- ✅ Controle de acesso baseado em roles
- ✅ Validação de dados e regras de negócio
- ✅ Interface responsiva e intuitiva
- ✅ Banco de dados H2 para desenvolvimento
- ✅ Logs detalhados para auditoria

## 🛠 Tecnologias Utilizadas

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework principal
- **Spring MVC** - Arquitetura web
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Hibernate** - ORM (Object-Relational Mapping)
- **H2 Database** - Banco de dados em memória
- **Maven** - Gerenciamento de dependências

### Frontend
- **Thymeleaf** - Template engine
- **Bootstrap 5.3.0** - Framework CSS
- **jQuery 3.7.0** - Biblioteca JavaScript
- **Bootstrap Icons** - Ícones
- **HTML5/CSS3** - Estrutura e estilização
- **JavaScript ES6+** - Interatividade

### Ferramentas de Desenvolvimento
- **Maven** - Build e gerenciamento de dependências
- **Spring Boot DevTools** - Hot reload
- **H2 Console** - Interface para banco de dados
- **SLF4J + Logback** - Sistema de logs

## 🏗 Arquitetura

O sistema segue o padrão **MVC (Model-View-Controller)** com as seguintes camadas:

```
src/main/java/com/sistema/aluguelcarros/
├── model/          # Entidades JPA
├── repository/     # Interfaces de acesso a dados
├── service/        # Lógica de negócio
├── controller/     # Controladores web
├── security/       # Configurações de segurança
└── config/         # Configurações gerais

src/main/resources/
├── templates/      # Templates Thymeleaf
├── static/         # Recursos estáticos (CSS, JS, imagens)
├── data.sql        # Script de inicialização
└── application.properties  # Configurações
```

### Entidades Principais

- **User** - Classe base para usuários
- **Client** - Cliente individual (herda de User)
- **Agent** - Agente/empresa (herda de User)
- **Car** - Veículo disponível para locação
- **RentalOrder** - Pedido de aluguel
- **Contract** - Contrato de locação
- **Employment** - Emprego/renda do cliente

## 🚀 Instalação e Execução

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Git (opcional)

### Passos para Instalação

1. **Clone o repositório** (ou extraia o arquivo ZIP):
```bash
git clone <url-do-repositorio>
cd sistema-aluguel-carros
```

2. **Compile o projeto**:
```bash
mvn clean compile
```

3. **Execute a aplicação**:
```bash
mvn spring-boot:run
```

4. **Acesse a aplicação**:
   - URL: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console

### Execução Alternativa

Você também pode gerar o JAR executável:

```bash
mvn clean package -DskipTests
java -jar target/aluguel-carros-1.0.0.jar
```

## ⚙️ Configuração

### Banco de Dados

O sistema utiliza H2 Database por padrão. As configurações estão em `application.properties`:

```properties
# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=password

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Dados de Teste

O sistema inclui dados de teste pré-carregados:

**Usuários de Teste:**
- **Cliente**: cliente@teste.com / 123456
- **Agente**: agente@teste.com / 123456
- **Banco**: banco@teste.com / 123456

### Configurações de Segurança

- Senhas criptografadas com BCrypt
- Sessões com timeout de 30 minutos
- Proteção CSRF habilitada
- Controle de acesso baseado em roles

## 📱 Uso do Sistema

### Primeiro Acesso

1. Acesse http://localhost:8080
2. Clique em "Cadastrar" para criar uma conta
3. Escolha entre Cliente ou Agente
4. Preencha os dados solicitados
5. Faça login com suas credenciais

### Fluxo do Cliente

1. **Cadastro**: Registre-se como cliente
2. **Empregos**: Adicione informações de emprego/renda
3. **Busca**: Navegue pelos carros disponíveis
4. **Pedido**: Crie um pedido de aluguel
5. **Acompanhamento**: Monitore o status do pedido
6. **Contrato**: Após aprovação, visualize o contrato

### Fluxo do Agente

1. **Cadastro**: Registre-se como empresa ou banco
2. **Gestão**: Gerencie clientes e veículos
3. **Avaliação**: Analise pedidos pendentes
4. **Aprovação**: Aprove ou rejeite solicitações
5. **Contratos**: Crie e gerencie contratos

## 🔌 API Endpoints

### Autenticação
- `GET /login` - Página de login
- `POST /login` - Processar login
- `POST /logout` - Logout
- `GET /register` - Página de cadastro
- `POST /register/client` - Cadastrar cliente
- `POST /register/agent` - Cadastrar agente

### Dashboard
- `GET /dashboard` - Dashboard principal
- `GET /profile` - Perfil do usuário

### Carros
- `GET /cars` - Listar todos os carros
- `GET /cars/available` - Carros disponíveis
- `GET /cars/{id}` - Detalhes do carro
- `GET /cars/new` - Formulário novo carro (agentes)
- `POST /cars` - Criar/atualizar carro (agentes)
- `POST /cars/{id}/delete` - Excluir carro (agentes)

### Pedidos
- `GET /orders` - Listar pedidos
- `GET /orders/{id}` - Detalhes do pedido
- `GET /orders/new` - Novo pedido (clientes)
- `POST /orders` - Criar pedido (clientes)
- `POST /orders/{id}/cancel` - Cancelar pedido
- `GET /orders/{id}/evaluate` - Avaliar pedido (agentes)
- `POST /orders/{id}/evaluate` - Processar avaliação (agentes)

### Clientes (Agentes apenas)
- `GET /clients` - Listar clientes
- `GET /clients/{id}` - Detalhes do cliente
- `GET /clients/new` - Novo cliente
- `POST /clients` - Criar/atualizar cliente
- `POST /clients/{id}/delete` - Excluir cliente

### Contratos (Agentes apenas)
- `GET /contracts` - Listar contratos
- `GET /contracts/{id}` - Detalhes do contrato
- `GET /contracts/new` - Novo contrato
- `POST /contracts` - Criar contrato
- `POST /contracts/{id}/complete` - Concluir contrato

## 🗄️ Banco de Dados

### Modelo de Dados

O sistema utiliza as seguintes tabelas principais:

- `users` - Usuários base
- `clients` - Dados específicos de clientes
- `agents` - Dados específicos de agentes
- `employments` - Empregos dos clientes
- `cars` - Veículos
- `rental_orders` - Pedidos de aluguel
- `contracts` - Contratos
- `order_evaluations` - Avaliações de pedidos

### Relacionamentos

- Cliente → Empregos (1:N)
- Cliente → Pedidos (1:N)
- Carro → Pedidos (1:N)
- Pedido → Contrato (1:1)
- Agente → Avaliações (1:N)
- Banco → Contratos de Crédito (1:N)

## 🔒 Segurança

### Autenticação
- Login baseado em email/senha
- Senhas criptografadas com BCrypt
- Sessões seguras com cookies HTTP-only

### Autorização
- **ROLE_CLIENT**: Acesso a funcionalidades de cliente
- **ROLE_AGENT**: Acesso a funcionalidades de agente
- **ROLE_ADMIN**: Acesso administrativo completo

### Proteções
- Proteção CSRF
- Validação de entrada
- Controle de acesso por URL
- Logs de segurança

## 🧪 Testes

### Executar Testes
```bash
mvn test
```

### Cobertura de Testes
```bash
mvn test jacoco:report
```

### Dados de Teste
O sistema inclui dados de teste abrangentes para:
- Usuários (clientes, agentes, bancos)
- Veículos com diferentes status
- Pedidos em vários estágios
- Contratos simples e com crédito

## 📝 Estrutura do Projeto

```
sistema-aluguel-carros/
├── src/
│   ├── main/
│   │   ├── java/com/sistema/aluguelcarros/
│   │   │   ├── config/          # Configurações
│   │   │   ├── controller/      # Controllers
│   │   │   ├── model/           # Entidades
│   │   │   ├── repository/      # Repositórios
│   │   │   ├── security/        # Segurança
│   │   │   ├── service/         # Serviços
│   │   │   └── AluguelCarrosApplication.java
│   │   └── resources/
│   │       ├── static/          # CSS, JS, imagens
│   │       ├── templates/       # Templates Thymeleaf
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/                    # Testes
├── target/                      # Arquivos compilados
├── pom.xml                      # Configuração Maven
└── README.md                    # Esta documentação
```

## 🤝 Contribuição

Para contribuir com o projeto:

1. Fork o repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

### Padrões de Código
- Siga as convenções Java
- Use nomes descritivos para variáveis e métodos
- Adicione comentários para lógica complexa
- Mantenha os métodos pequenos e focados
- Escreva testes para novas funcionalidades

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 📞 Suporte

Para suporte ou dúvidas:
- Abra uma issue no repositório
- Consulte a documentação
- Verifique os logs da aplicação

---

**Desenvolvido com ❤️ usando Spring Boot e tecnologias modernas**

