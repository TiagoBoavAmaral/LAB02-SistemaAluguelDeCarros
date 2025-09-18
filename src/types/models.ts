export interface Usuario {
  id: number;
  login: string;
  senha: string;
  perfil: 'CLIENTE' | 'AGENTE';
}

export interface Cliente extends Usuario {
  rg: string;
  cpf: string;
  nome: string;
  endereco: string;
  profissao: string;
  carrosAlugados: CarroAlugado[];
}

export interface CarroAlugado {
  id: number;
  marca: string;
  modelo: string;
  placa: string;
  dataInicio: Date;
  dataFim: Date;
  valor: number;
}

export interface Automovel {
  id: number;
  matricula: string;
  placa: string;
  marca: string;
  modelo: string;
  ano: number;
  proprietario: Proprietario;
}

export interface Proprietario {
  tipo: 'CLIENTE' | 'EMPRESA' | 'BANCO';
  referenciaId: number;
}

export interface Agente extends Usuario {
  nome: string;
  tipo: 'EMPRESA' | 'BANCO';
  contato: string;
}

export enum PedidoStatus {
  RASCUNHO = 'RASCUNHO',
  ENVIADO = 'ENVIADO', 
  EM_ANALISE = 'EM_ANALISE',
  APROVADO = 'APROVADO',
  REPROVADO = 'REPROVADO',
  CANCELADO = 'CANCELADO'
}

export interface Pedido {
  id: number;
  dataInicio: Date;
  dataFim: Date;
  status: PedidoStatus;
  valor: number;
  clienteId: number;
  automovelId: number;
}

export interface Contrato {
  id: number;
  tipo: string;
  dataAssinatura: Date;
  valorFinanciado: number;
  pedidoId: number;
}

export type FormError = {
  field: string;
  message: string;
};