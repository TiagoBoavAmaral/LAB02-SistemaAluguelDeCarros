# Sistema de Aluguel de Carros

![Java](https://img.shields.io/badge/Java-17+-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.5-success?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apache-maven)

Este projeto é um sistema web para apoiar a gestão de aluguéis de automóveis, permitindo que clientes solicitem aluguéis e que agentes da empresa avaliem e processem esses pedidos. O sistema foi desenvolvido como parte do Laboratório de Desenvolvimento de Software da PUC Minas.

Alunos: Tiago Boaventura Amaral e Kayque Allan Ribeiro Freitas

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

Para instruções detalhadas sobre como executar o projeto, consulte o arquivo [COMO-EXECUTAR.md](COMO-EXECUTAR.md).

```markdown
## 📋 Pré-requisitos

### Java
- **Java 17 ou superior** é obrigatório

### Verificar se Java está instalado:
```bash
java -version
```

## 🖥️ Windows

### Método 1: Script Automático (Recomendado)
```bash
# Clique duplo no arquivo ou execute no terminal:
run-windows.bat
```

### Método 2: Manual
```cmd
# 1. Abrir PowerShell/CMD como Administrador
# 2. Navegar para a pasta do projeto
cd "C:\caminho\para\LAB02-SistemaAluguelDeCarros"

# 3. Entrar na pasta rental-system
cd rental-system

# 4. Executar a aplicação
.\mvnw.cmd spring-boot:run
```

## 🐧 Linux / 🍎 macOS

### Método 1: Script Automático (Recomendado)
```bash
# Dar permissão de execução (apenas na primeira vez)
chmod +x run-linux-mac.sh

# Executar o script
./run-linux-mac.sh
```

### Método 2: Manual
```bash
# 1. Abrir terminal
# 2. Navegar para a pasta do projeto
cd /caminho/para/LAB02-SistemaAluguelDeCarros

# 3. Entrar na pasta rental-system
cd rental-system

# 4. Dar permissão ao Maven wrapper
chmod +x mvnw

# 5. Executar a aplicação
./mvnw spring-boot:run
```

## 🌐 Acessar a Aplicação

Após iniciar, acesse: **http://localhost:8080**

### Credenciais de Teste:
- **Cliente**: cliente@email.com / password
- **Agente**: agente@email.com / password

## 🔧 Solução de Problemas

### Erro: "Java não encontrado"
- Instale Java 17+
- Verifique se JAVA_HOME está configurado

### Erro: "Maven wrapper não encontrado"
- Certifique-se de estar na pasta correta
- Execute os scripts do diretório raiz do projeto

### Erro: "Porta 8080 já está em uso"
- Pare outras aplicações na porta 8080
- Ou altere a porta em `application.properties`:
  ```properties
  server.port=8081
  ```

### Erro de versão do Java
- Spring Boot 3.1.5 precisa Java 17+
- Verifique: `java -version`
- Configure JAVA_HOME se necessário

## 📁 Estrutura do Projeto
```
LAB02-SistemaAluguelDeCarros/
├── run-windows.bat          # Script para Windows
├── run-linux-mac.sh         # Script para Linux/macOS
├── COMO-EXECUTAR.md         # Este arquivo
└── rental-system/           # Projeto Spring Boot
    ├── mvnw                 # Maven wrapper (Linux/macOS)
    ├── mvnw.cmd             # Maven wrapper (Windows)
    └── pom.xml              # Configuração Maven
```

## 💡 Dicas

1. **Use os scripts automáticos** - eles verificam tudo automaticamente
2. **Mantenha Java atualizado** - sempre use Java 17 ou superior
3. **Execute como Administrador** no Windows se necessário
4. **Verifique a porta 8080** - não deve estar em uso por outras aplicações
```


## 🏛️ Estrutura do Projeto

O código está organizado seguindo as camadas da arquitetura MVC, conforme a especificação do projeto:

* `com.pucminas.rental_system.config`: Configurações de segurança (Spring Security).
* `com.pucminas.rental_system.controller`: Classes que recebem as requisições web.
* `com.pucminas.rental_system.model`: Entidades JPA que representam os dados do sistema.
* `com.pucminas.rental_system.repository`: Interfaces do Spring Data JPA para acesso ao banco de dados.
* `com.pucminas.rental_system.service`: Classes que contêm a lógica de negócio do sistema.
