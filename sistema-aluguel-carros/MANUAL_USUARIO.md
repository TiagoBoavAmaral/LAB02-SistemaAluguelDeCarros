# Manual do Usuário - Sistema de Aluguel de Carros

## 📖 Guia Completo de Utilização

Este manual fornece instruções detalhadas sobre como usar o Sistema de Aluguel de Carros, desde o primeiro acesso até a conclusão de contratos.

## 🎯 Visão Geral do Sistema

O Sistema de Aluguel de Carros permite que:
- **Clientes** solicitem aluguel de veículos
- **Agentes** (empresas/bancos) gerenciem o processo de aprovação
- **Administradores** controlem todo o sistema

## 🚀 Primeiro Acesso

### 1. Acessando o Sistema
1. Abra seu navegador web
2. Digite o endereço: `http://localhost:8080`
3. Você verá a página inicial do sistema

### 2. Criando uma Conta

#### Para Clientes (Pessoas Físicas)
1. Clique em **"Cadastrar"** na página inicial
2. Selecione a aba **"Cliente"**
3. Preencha os dados obrigatórios:
   - Nome completo
   - Email (será seu login)
   - Senha (mínimo 6 caracteres)
   - CPF (apenas números)
   - RG
   - Profissão
4. Preencha dados opcionais:
   - Telefone
   - Endereço
5. Clique em **"Cadastrar como Cliente"**

#### Para Agentes (Empresas/Bancos)
1. Clique em **"Cadastrar"** na página inicial
2. Selecione a aba **"Agente/Empresa"**
3. Preencha os dados obrigatórios:
   - Nome do responsável
   - Email corporativo
   - Senha
   - CPF do responsável
   - RG do responsável
   - CNPJ da empresa
   - Nome da empresa
   - Tipo de agente (Empresa ou Banco)
4. Preencha dados opcionais:
   - Telefone
   - Endereço
5. Clique em **"Cadastrar como Agente"**

### 3. Fazendo Login
1. Na página inicial, clique em **"Entrar"**
2. Digite seu email e senha
3. Clique em **"Entrar"**
4. Você será redirecionado para o dashboard

## 👤 Guia para Clientes

### Dashboard do Cliente
Após o login, você verá:
- Estatísticas dos seus pedidos
- Ações rápidas (Novo Pedido, Ver Carros, etc.)
- Seus pedidos recentes
- Carros disponíveis

### Gerenciando Empregos
**⚠️ Importante**: Para fazer pedidos, você precisa cadastrar pelo menos um emprego.

1. Acesse **"Perfil"** no menu superior
2. Clique em **"Adicionar Emprego"**
3. Preencha:
   - Nome da empresa
   - Cargo/posição
   - Salário mensal
   - Data de início
   - Se é o emprego atual
4. Clique em **"Salvar"**

**Limite**: Máximo 3 empregos por cliente.

### Procurando Carros
1. Clique em **"Carros"** no menu ou **"Ver Carros Disponíveis"**
2. Use os filtros para encontrar o carro ideal:
   - Marca
   - Modelo
   - Ano
   - Diária máxima
3. Clique em **"Buscar"**
4. Visualize os resultados em grade ou lista

### Criando um Pedido de Aluguel
1. Encontre o carro desejado
2. Clique em **"Alugar Agora"** ou **"Ver Detalhes"** → **"Alugar"**
3. No formulário de pedido:
   - Selecione o carro (se não pré-selecionado)
   - Escolha a data de início
   - Escolha a data de fim
   - Adicione observações (opcional)
4. Verifique o valor total calculado automaticamente
5. Clique em **"Criar Pedido"**

### Acompanhando Pedidos
1. Acesse **"Meus Pedidos"** no menu
2. Veja todos os seus pedidos com status:
   - **Pendente**: Aguardando avaliação
   - **Em Avaliação**: Sendo analisado por agente
   - **Aprovado**: Pedido aprovado, contrato será criado
   - **Rejeitado**: Pedido negado
   - **Ativo**: Contrato ativo
   - **Concluído**: Aluguel finalizado
   - **Cancelado**: Pedido cancelado

### Cancelando Pedidos
1. Na lista de pedidos, clique no ícone de cancelamento (❌)
2. Informe o motivo (opcional)
3. Confirme o cancelamento

**Nota**: Apenas pedidos pendentes ou em avaliação podem ser cancelados.

### Visualizando Contratos
1. Quando um pedido é aprovado, um contrato é criado automaticamente
2. Acesse **"Meus Pedidos"** → clique no pedido aprovado
3. Veja os detalhes do contrato, incluindo:
   - Tipo de contrato (simples ou com crédito)
   - Valores e condições
   - Datas de vigência

## 🏢 Guia para Agentes

### Dashboard do Agente
Após o login, você verá:
- Estatísticas gerais do sistema
- Pedidos pendentes de avaliação
- Ações rápidas para gerenciamento

### Gerenciando Clientes
1. Acesse **"Gerenciar"** → **"Clientes"**
2. Veja a lista de todos os clientes
3. Para cada cliente, você pode:
   - **Ver detalhes**: Informações completas e empregos
   - **Editar**: Modificar dados do cliente
   - **Ativar/Desativar**: Controlar acesso do cliente

#### Adicionando Empregos para Clientes
1. Na página de detalhes do cliente
2. Clique em **"Adicionar Emprego"**
3. Preencha os dados do emprego
4. Salve as informações

### Gerenciando Carros
1. Acesse **"Gerenciar"** → **"Novo Carro"** ou **"Carros"**
2. Para adicionar um carro:
   - Matrícula (código interno)
   - Marca e modelo
   - Ano
   - Placa
   - Cor
   - Diária (valor por dia)
   - Status inicial
   - Tipo de proprietário

#### Status dos Carros
- **Disponível**: Pronto para aluguel
- **Alugado**: Atualmente em uso
- **Manutenção**: Em reparo/manutenção
- **Indisponível**: Temporariamente fora de serviço

### Avaliando Pedidos
1. Acesse **"Gerenciar"** → **"Pedidos Pendentes"**
2. Para cada pedido, clique em **"Avaliar"**
3. Analise as informações:
   - Dados do cliente
   - Histórico de empregos
   - Renda total
   - Detalhes do pedido
4. Tome uma decisão:
   - **Aprovar**: Pedido será aprovado
   - **Rejeitar**: Pedido será negado
   - **Em Avaliação**: Manter em análise
5. Adicione notas de avaliação
6. Clique em **"Salvar Avaliação"**

### Criando Contratos
Após aprovar um pedido:

#### Contrato Simples
1. Acesse o pedido aprovado
2. Clique em **"Criar Contrato"**
3. Selecione **"Aluguel Simples"**
4. Adicione termos específicos (opcional)
5. Confirme a criação

#### Contrato com Crédito (Bancos)
1. Acesse o pedido aprovado
2. Clique em **"Criar Contrato"**
3. Selecione **"Aluguel com Crédito"**
4. Preencha:
   - Valor do crédito
   - Taxa de juros
   - Banco responsável
5. Adicione termos específicos
6. Confirme a criação

### Gerenciando Contratos
1. Acesse **"Contratos"**
2. Para cada contrato, você pode:
   - **Visualizar**: Ver detalhes completos
   - **Concluir**: Finalizar contrato ativo
   - **Suspender**: Pausar temporariamente
   - **Cancelar**: Cancelar definitivamente

## 🔍 Funcionalidades Especiais

### Busca e Filtros
- Use a barra de busca para encontrar rapidamente
- Combine múltiplos filtros para resultados precisos
- Limpe os filtros para ver todos os resultados

### Notificações
- Mensagens de sucesso aparecem em verde
- Mensagens de erro aparecem em vermelho
- Mensagens informativas aparecem em azul
- Todas desaparecem automaticamente após 5 segundos

### Responsividade
- O sistema funciona em desktop, tablet e celular
- Menus se adaptam ao tamanho da tela
- Tabelas se tornam scrolláveis em telas pequenas

## ⚠️ Regras Importantes

### Para Clientes
- Máximo de 3 empregos por cliente
- Pelo menos 1 emprego é obrigatório para fazer pedidos
- Data de início deve ser futura
- Data de fim deve ser posterior à data de início
- Pedidos só podem ser cancelados antes da aprovação

### Para Agentes
- Empresas podem gerenciar todos os aspectos
- Bancos têm funcionalidades especiais para crédito
- Carros alugados não podem ser excluídos
- Contratos ativos não podem ser modificados

### Geral
- Senhas devem ter pelo menos 6 caracteres
- CPF e CNPJ devem ser válidos
- Emails devem ser únicos no sistema
- Sessão expira após 30 minutos de inatividade

## 🔧 Solução de Problemas

### Problemas de Login
- Verifique se o email está correto
- Certifique-se de que a senha está correta
- Tente limpar o cache do navegador
- Verifique se sua conta está ativa

### Problemas com Pedidos
- Certifique-se de ter pelo menos um emprego cadastrado
- Verifique se as datas são válidas
- Confirme se o carro ainda está disponível
- Tente atualizar a página

### Problemas de Performance
- Feche outras abas do navegador
- Limpe o cache e cookies
- Tente usar outro navegador
- Verifique sua conexão com a internet

## 📞 Suporte

Se você encontrar problemas não cobertos neste manual:

1. Verifique os logs do sistema (para desenvolvedores)
2. Tente reproduzir o problema
3. Documente os passos que levaram ao erro
4. Entre em contato com o suporte técnico

## 💡 Dicas de Uso

### Para Melhor Experiência
- Use navegadores modernos (Chrome, Firefox, Safari, Edge)
- Mantenha os dados sempre atualizados
- Faça logout ao terminar de usar
- Use senhas seguras

### Atalhos Úteis
- **Ctrl + F**: Buscar na página
- **F5**: Atualizar página
- **Ctrl + Shift + R**: Atualizar ignorando cache
- **Tab**: Navegar entre campos de formulário

---

**Este manual cobre as principais funcionalidades do sistema. Para funcionalidades avançadas ou questões específicas, consulte a documentação técnica ou entre em contato com o suporte.**

