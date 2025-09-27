# Sistema de Aluguel de Carros

![Java](https://img.shields.io/badge/Java-17+-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-success?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apache-maven)

Este projeto é um sistema web para apoiar a gestão de aluguéis de automóveis, permitindo que clientes solicitem aluguéis e que agentes da empresa avaliem e processem esses pedidos. O sistema foi desenvolvido como parte do Laboratório de Desenvolvimento de Software da PUC Minas.

## 📋 Funcionalidades

O protótipo atual implementa o fluxo principal do sistema, incluindo:

* **Autenticação e Autorização**: O sistema requer cadastro prévio e possui dois níveis de acesso distintos: **Cliente** e **Agente**.
* **Gestão de Pedidos pelo Cliente**: Usuários do tipo "Cliente" podem criar, modificar, consultar e cancelar seus pedidos de aluguel.
* **Avaliação de Pedidos pelo Agente**: Usuários do tipo "Agente" (representando empresas ou bancos) podem visualizar todos os pedidos pendentes e realizar a análise financeira para aprová-los ou rejeitá-los.
* **Persistência de Dados**: O sistema armazena informações sobre contratantes (clientes), automóveis e os próprios pedidos de aluguel.
* **Dashboard do Cliente**: Interface com menu lateral e área principal mostrando resumo completo dos pedidos e estatísticas.
* **Dashboard do Agente**: Interface para agentes com visão geral de todos os pedidos e ferramentas de análise.
* **Gestão de Veículos**: Sistema completo de cadastro, edição e exclusão de automóveis (apenas para agentes).

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias, conforme a especificação do projeto:

* **Backend**:
    * Java 17+
    * Spring Boot 3.1.5
    * Spring MVC
    * Spring Security (para autenticação e autorização)
    * Spring Data JPA / Hibernate (para persistência de dados)
* **Frontend**:
    * Thymeleaf (motor de templates para renderizar as páginas web)
    * CSS3 com tema claro corporativo e design responsivo
    * Interface profissional com cores neutras e elementos visuais limpos
* **Banco de Dados**:
    * H2 Database (banco de dados em memória para ambiente de desenvolvimento)
* **Build Tool**:
    * Maven

## 🚀 Como Executar o Projeto

Para executar este projeto em sua máquina local, siga os passos abaixo.

### Pré-requisitos

* **JDK 17** ou superior instalado.
* **Apache Maven** instalado e configurado no PATH do sistema.

### Passos para Execução

1.  **Clone o repositório** (ou certifique-se de ter a pasta do projeto em sua máquina).
    ```bash
    git clone https://github.com/TiagoBoavAmaral/LAB02-SistemaAluguelDeCarros.git
    ```

2.  **Navegue até a pasta raiz do projeto** (a pasta que contém o arquivo `pom.xml`).
    ```bash
    cd rental-system
    ```

3.  **Execute o projeto usando o Maven**:
    ```bash
    mvn spring-boot:run
    ```

    Alternativamente, você pode importar o projeto em sua IDE preferida (IntelliJ IDEA, Eclipse, VS Code) e executar a classe principal `RentalSystemApplication.java`.

4.  **Acesse a aplicação**: Após a inicialização, o sistema estará disponível em `http://localhost:8080`.

## 🧪 Como Testar a Aplicação

O banco de dados é inicializado com dois usuários de teste para demonstrar o fluxo completo.

#### 1. **Acesse a tela de login**:
* Abra o navegador e acesse: `http://localhost:8080`

#### 2. **Login como Cliente**:
* **Email**: `cliente@email.com`
* **Senha**: `password`
* **Ações**:
    1.  Clique em "Fazer Novo Pedido".
    2.  Selecione um carro e envie o pedido.
    3.  Verifique se o novo pedido aparece na lista com o status "PENDENTE".
    4.  Faça logout.

#### 3. **Login como Agente**:
* **Email**: `agente@email.com`
* **Senha**: `password`
* **Ações**:
    1.  Você verá o pedido criado pelo cliente na lista de "Pedidos Pendentes".
    2.  Clique no botão "Aprovar". O pedido deverá sumir da lista.
    3.  Faça logout.

#### 4. **Verificação Final**:
* Faça login novamente como **Cliente**.
* Verifique se o status do seu pedido foi atualizado para **"APROVADO"**.

#### Acesso ao Banco de Dados H2
Para inspecionar os dados diretamente:
* **URL**: `http://localhost:8080/h2-console`
* **JDBC URL**: `jdbc:h2:mem:rentalsystemdb`
* **User Name**: `sa`
* **Password**: `password`

## 🏛️ Estrutura do Projeto

O código está organizado seguindo as camadas da arquitetura MVC, conforme a especificação do projeto:

* `com.pucminas.rental_system.config`: Configurações de segurança (Spring Security).
* `com.pucminas.rental_system.controller`: Classes que recebem as requisições web.
* `com.pucminas.rental_system.model`: Entidades JPA que representam os dados do sistema.
* `com.pucminas.rental_system.repository`: Interfaces do Spring Data JPA para acesso ao banco de dados.
* `com.pucminas.rental_system.service`: Classes que contêm a lógica de negócio do sistema.

## 🎨 Melhorias de Design

O sistema foi aprimorado com um design profissional e responsivo que inclui:

### ✨ Características Visuais
- **Tema Claro**: Interface com cores neutras e design corporativo
- **Paleta Profissional**: Uso de azuis, cinzas e brancos para um visual sério
- **Animações Sutis**: Transições suaves e efeitos hover discretos
- **Design Responsivo**: Adaptação automática para diferentes tamanhos de tela
- **Tipografia Profissional**: Uso da fonte Segoe UI para melhor legibilidade

### 🎯 Elementos de Interface
- **Cards Limpos**: Containers com bordas sutis e sombras discretas
- **Botões Corporativos**: Design limpo com cores padrão do Bootstrap
- **Tabelas Profissionais**: Layout organizado com hover effects e status badges
- **Formulários Elegantes**: Campos de entrada com focus states e validação visual
- **Alertas Informativos**: Mensagens de sucesso e erro com cores apropriadas

### 📱 Responsividade
- **Mobile-First**: Design otimizado para dispositivos móveis
- **Breakpoints**: Adaptação para tablets e desktops
- **Navegação Flexível**: Menu e botões que se adaptam ao espaço disponível

### 🎛️ Dashboard do Cliente
- **Menu Lateral**: Navegação intuitiva com todas as funcionalidades
- **Cards de Resumo**: Estatísticas visuais dos pedidos (total, ativos, rejeitados, pendentes)
- **Tabela de Pedidos Recentes**: Visualização rápida dos últimos pedidos
- **Ações Rápidas**: Botões para acesso direto às principais funcionalidades
- **Páginas Especializadas**: Meus Dados e Automóveis Disponíveis com layouts otimizados

### 🎛️ Dashboard do Agente
- **Menu Lateral Profissional**: Navegação específica para agentes
- **Cards de Resumo**: Estatísticas de todos os pedidos (pendentes, aprovados, rejeitados, total)
- **Tabela de Pedidos Recentes**: Visualização de todos os pedidos do sistema
- **Páginas Especializadas**: Meus Dados, Todos os Pedidos e Automóveis Cadastrados
- **Ferramentas de Análise**: Acesso direto à avaliação de pedidos pendentes

### 🚗 Gestão de Veículos (Apenas Agentes)
- **Cadastro de Veículos**: Formulário completo com validações (Matrícula, Ano, Marca, Modelo, Placa, Cor)
- **Edição de Veículos**: Atualização de dados existentes com formulário pré-preenchido
- **Exclusão de Veículos**: Remoção com confirmação de segurança
- **Validações**: Campos obrigatórios, formato de placa, faixa de anos válida
- **Interface Responsiva**: Formulários adaptados para mobile e desktop
