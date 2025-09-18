import { Cliente, CarroAlugado, Usuario, FormError } from '@/types/models';

class ClienteService {
  private clientes: Cliente[] = [
    {
      id: 1,
      login: 'joao.silva@email.com',
      senha: '123456',
      perfil: 'CLIENTE',
      rg: '12.345.678-9',
      cpf: '123.456.789-00',
      nome: 'João Silva Santos',
      endereco: 'Rua das Flores, 123 - Centro - Belo Horizonte/MG',
      profissao: 'Engenheiro de Software',
      carrosAlugados: [
        { id: 1, marca: 'Toyota', modelo: 'Corolla', placa: 'ABC-1234', dataInicio: new Date('2024-01-15'), dataFim: new Date('2024-01-20'), valor: 350.00 },
        { id: 2, marca: 'Honda', modelo: 'Civic', placa: 'XYZ-5678', dataInicio: new Date('2024-02-10'), dataFim: new Date('2024-02-15'), valor: 400.00 }
      ]
    },
    {
      id: 2,
      login: 'maria.santos@email.com',
      senha: '123456',
      perfil: 'CLIENTE',
      rg: '98.765.432-1',
      cpf: '987.654.321-00',
      nome: 'Maria Santos Oliveira',
      endereco: 'Av. Amazonas, 456 - Funcionários - Belo Horizonte/MG',
      profissao: 'Médica',
      carrosAlugados: [
        { id: 3, marca: 'Volkswagen', modelo: 'Jetta', placa: 'DEF-9012', dataInicio: new Date('2024-03-05'), dataFim: new Date('2024-03-12'), valor: 450.00 }
      ]
    }
  ];

  private nextId = 3;
  private nextCarroAlugadoId = 5;

  // Simular delay de rede
  private delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

  async listarClientes(): Promise<Cliente[]> {
    await this.delay(500);
    return [...this.clientes];
  }

  async buscarClientePorId(id: number): Promise<Cliente | null> {
    await this.delay(300);
    return this.clientes.find(c => c.id === id) || null;
  }

  async criarCliente(dadosCliente: Omit<Cliente, 'id'>): Promise<Cliente> {
    await this.delay(800);
    
    // Validar se CPF já existe
    if (this.clientes.some(c => c.cpf === dadosCliente.cpf)) {
      throw new Error('CPF já cadastrado no sistema');
    }
    
    // Validar se login já existe
    if (this.clientes.some(c => c.login === dadosCliente.login)) {
      throw new Error('Email já cadastrado no sistema');
    }

    const novoCliente: Cliente = {
      ...dadosCliente,
      id: this.nextId++,
      carrosAlugados: dadosCliente.carrosAlugados.map(c => ({
        ...c,
        id: c.id || this.nextCarroAlugadoId++
      }))
    };

    this.clientes.push(novoCliente);
    return novoCliente;
  }

  async atualizarCliente(id: number, dadosCliente: Partial<Cliente>): Promise<Cliente> {
    await this.delay(800);
    
    const index = this.clientes.findIndex(c => c.id === id);
    if (index === -1) {
      throw new Error('Cliente não encontrado');
    }

    // Validar CPF se foi alterado
    if (dadosCliente.cpf && dadosCliente.cpf !== this.clientes[index].cpf) {
      if (this.clientes.some(c => c.id !== id && c.cpf === dadosCliente.cpf)) {
        throw new Error('CPF já cadastrado no sistema');
      }
    }

    // Validar login se foi alterado
    if (dadosCliente.login && dadosCliente.login !== this.clientes[index].login) {
      if (this.clientes.some(c => c.id !== id && c.login === dadosCliente.login)) {
        throw new Error('Email já cadastrado no sistema');
      }
    }

    const clienteAtualizado = {
      ...this.clientes[index],
      ...dadosCliente,
      carrosAlugados: dadosCliente.carrosAlugados?.map(c => ({
        ...c,
        id: c.id || this.nextCarroAlugadoId++
      })) || this.clientes[index].carrosAlugados
    };

    this.clientes[index] = clienteAtualizado;
    return clienteAtualizado;
  }

  async excluirCliente(id: number): Promise<void> {
    await this.delay(500);
    
    const index = this.clientes.findIndex(c => c.id === id);
    if (index === -1) {
      throw new Error('Cliente não encontrado');
    }

    this.clientes.splice(index, 1);
  }

  validarDadosCliente(dados: Partial<Cliente>): FormError[] {
    const erros: FormError[] = [];

    if (!dados.nome || dados.nome.trim().length < 2) {
      erros.push({ field: 'nome', message: 'Nome deve ter pelo menos 2 caracteres' });
    }

    if (!dados.cpf || !/^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(dados.cpf)) {
      erros.push({ field: 'cpf', message: 'CPF deve estar no formato 000.000.000-00' });
    }

    if (!dados.rg || dados.rg.trim().length < 5) {
      erros.push({ field: 'rg', message: 'RG é obrigatório' });
    }

    if (!dados.login || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(dados.login)) {
      erros.push({ field: 'login', message: 'Email deve ter formato válido' });
    }

    if (!dados.endereco || dados.endereco.trim().length < 10) {
      erros.push({ field: 'endereco', message: 'Endereço deve ter pelo menos 10 caracteres' });
    }

    if (!dados.profissao || dados.profissao.trim().length < 2) {
      erros.push({ field: 'profissao', message: 'Profissão é obrigatória' });
    }

    return erros;
  }
}

export const clienteService = new ClienteService();