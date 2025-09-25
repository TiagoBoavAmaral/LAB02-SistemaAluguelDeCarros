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
    # Exemplo:
    git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/seu-usuario/seu-repositorio.git)
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
