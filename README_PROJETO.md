# Sistema de Aluguel de Carros - CarRental

## 📋 Sobre o Projeto

Sistema web desenvolvido para a disciplina **Laboratório de Desenvolvimento de Software** da PUC Minas, implementando um CRUD completo de clientes seguindo a arquitetura MVC para um sistema de aluguel de carros.

## 🏗️ Arquitetura MVC Implementada

### Model (Modelos)
- **Cliente**: Entidade principal com dados pessoais, profissionais e financeiros
- **Usuario**: Base para autenticação e controle de acesso  
- **Rendimento**: Informações financeiras dos clientes (até 3 fontes)
- **Automovel**: Dados dos veículos disponíveis
- **Pedido**: Solicitações de aluguel com status de aprovação
- **Contrato**: Formalização dos aluguéis aprovados

### View (Visualização)
- **Login**: Tela de autenticação com credenciais demo
- **Dashboard**: Painel principal com estatísticas e listagem
- **ClienteForm**: Formulário para criar/editar clientes
- **ClienteTable**: Tabela responsiva com ações CRUD
- **ClienteModal**: Modal para visualização detalhada

### Controller (Controle)
- **AuthService**: Gerenciamento de autenticação e sessões
- **ClienteService**: Operações CRUD com validações de negócio
- **Validações**: Verificação de CPF, email únicos e dados obrigatórios

## ✨ Funcionalidades Implementadas

### Sprint Lab02S02 - CRUD de Cliente
- ✅ **Criar Cliente**: Formulário completo com validações
- ✅ **Listar Clientes**: Tabela com busca e filtros
- ✅ **Visualizar Cliente**: Modal com todos os detalhes
- ✅ **Editar Cliente**: Atualização de dados existentes
- ✅ **Excluir Cliente**: Remoção com confirmação
- ✅ **Autenticação**: Login/logout com controle de sessão

### Recursos Adicionais
- 📊 **Dashboard**: Estatísticas de clientes e rendimentos
- 🔍 **Busca Avançada**: Por nome, CPF, email ou profissão
- 💰 **Gestão Financeira**: Até 3 fontes de rendimento por cliente
- 📱 **Design Responsivo**: Interface adaptável para mobile
- 🎨 **Design System**: Cores, gradientes e componentes padronizados

## 🎨 Design System

### Paleta de Cores
- **Primária**: Azul corporativo (#2563eb)
- **Secundária**: Cinza claro para backgrounds
- **Accent**: Azul cyan para destaques financeiros
- **Gradientes**: Linear gradients para elementos importantes

### Componentes UI
- Cards com sombras suaves
- Botões com estados hover/focus
- Formulários com validação visual
- Tabelas responsivas
- Modais acessíveis

## 🚀 Tecnologias Utilizadas

- **React 18** - Interface reativa
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Estilização utilitária
- **Shadcn/ui** - Componentes acessíveis
- **React Hook Form** - Gerenciamento de formulários
- **React Router** - Navegação SPA
- **Lucide React** - Ícones modernos

## 📊 Histórias de Usuário Atendidas

- **US01**: ✅ Cadastro no sistema
- **US02**: ✅ Autenticação (login/senha)
- **US07**: ✅ Inclusão de dados financeiros e rendimentos

### Funcionalidades Futuras (Sprint 03)
- **US03**: Cadastrar pedido de aluguel
- **US04**: Modificar pedido existente  
- **US05**: Cancelar pedido
- **US06**: Consultar status dos pedidos
- **US08**: Análise financeira por agentes
- **US09**: Atribuição de contratos
- **US10**: Registro de automóveis

## 🔐 Credenciais Demo

Para testar o sistema, utilize:
- **Email**: joao.silva@email.com
- **Senha**: 123456

## 📱 Como Usar

1. **Login**: Acesse com as credenciais demo ou clique em "Usar Login Demo"
2. **Dashboard**: Visualize estatísticas gerais e lista de clientes
3. **Novo Cliente**: Clique em "Novo Cliente" para adicionar
4. **Buscar**: Use a barra de busca para filtrar clientes
5. **Ações**: Use os ícones na tabela para visualizar, editar ou excluir

## 🏛️ Padrões Arquiteturais

### Separation of Concerns
- **Services**: Lógica de negócio isolada
- **Components**: Componentes reutilizáveis
- **Types**: Interfaces TypeScript centralizadas
- **Utils**: Funções utilitárias compartilhadas

### Boas Práticas
- Estados locais com useState/useEffect
- Validações client-side e server-side
- Tratamento de erros com toast notifications
- Loading states para melhor UX
- Responsividade mobile-first

## 📈 Próximos Passos (Sprint 03)

1. **Integração Backend**: Conectar com API real
2. **Módulo Pedidos**: Implementar gestão de solicitações
3. **Painel Agentes**: Interface para análise e aprovação
4. **Relatórios**: Dashboard com métricas avançadas
5. **Notificações**: Sistema de alertas em tempo real

---

**Desenvolvido com ❤️ para PUC Minas - Engenharia de Software**