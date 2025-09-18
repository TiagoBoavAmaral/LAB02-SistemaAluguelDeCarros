import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Header } from '@/components/layout/Header';
import { ClienteTable } from '@/components/tables/ClienteTable';
import { ClienteForm } from '@/components/forms/ClienteForm';
import { ClienteModal } from '@/components/modals/ClienteModal';
import { Plus, Search, Users, DollarSign, TrendingUp, Filter } from 'lucide-react';
import { Cliente } from '@/types/models';
import { clienteService } from '@/services/clienteService';
import { toast } from '@/hooks/use-toast';

type ViewMode = 'list' | 'create' | 'edit';

export default function Dashboard() {
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [filteredClientes, setFilteredClientes] = useState<Cliente[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [viewMode, setViewMode] = useState<ViewMode>('list');
  const [clienteEdicao, setClienteEdicao] = useState<Cliente | null>(null);
  const [clienteVisualizacao, setClienteVisualizacao] = useState<Cliente | null>(null);

  const carregarClientes = async () => {
    try {
      setLoading(true);
      const dadosClientes = await clienteService.listarClientes();
      setClientes(dadosClientes);
      setFilteredClientes(dadosClientes);
    } catch (error) {
      toast({
        variant: 'destructive',
        title: 'Erro ao carregar clientes',
        description: 'Não foi possível carregar a lista de clientes.'
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregarClientes();
  }, []);

  useEffect(() => {
    if (!searchTerm) {
      setFilteredClientes(clientes);
    } else {
      const filtered = clientes.filter(cliente =>
        cliente.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
        cliente.cpf.includes(searchTerm) ||
        cliente.login.toLowerCase().includes(searchTerm.toLowerCase()) ||
        cliente.profissao.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredClientes(filtered);
    }
  }, [searchTerm, clientes]);

  const handleNovoCliente = () => {
    setClienteEdicao(null);
    setViewMode('create');
  };

  const handleEditarCliente = (cliente: Cliente) => {
    setClienteEdicao(cliente);
    setViewMode('edit');
  };

  const handleVisualizarCliente = (cliente: Cliente) => {
    setClienteVisualizacao(cliente);
  };

  const handleClienteSalvo = (cliente: Cliente) => {
    carregarClientes();
    setViewMode('list');
    setClienteEdicao(null);
  };

  const handleCancelar = () => {
    setViewMode('list');
    setClienteEdicao(null);
  };

  const calcularEstatisticas = () => {
    const totalClientes = clientes.length;
    const gastoTotal = clientes.reduce((acc, cliente) => 
      acc + cliente.carrosAlugados.reduce((sum, c) => sum + c.valor, 0), 0
    );
    const gastoMedio = totalClientes > 0 ? gastoTotal / totalClientes : 0;
    
    return { totalClientes, gastoTotal, gastoMedio };
  };

  const { totalClientes, gastoTotal, gastoMedio } = calcularEstatisticas();

  const formatarMoeda = (valor: number): string => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background">
        <Header />
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary mx-auto mb-4"></div>
            <p className="text-muted-foreground">Carregando dados...</p>
          </div>
        </div>
      </div>
    );
  }

  if (viewMode === 'create' || viewMode === 'edit') {
    return (
      <div className="min-h-screen bg-background">
        <Header onNavigate={() => setViewMode('list')} />
        <div className="max-w-4xl mx-auto p-4 sm:p-6">
          <ClienteForm
            cliente={clienteEdicao}
            onSave={handleClienteSalvo}
            onCancel={handleCancelar}
          />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Header onNavigate={() => setViewMode('list')} />
      
      <div className="max-w-7xl mx-auto p-4 sm:p-6 space-y-6">
        {/* Header da Página */}
        <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center space-y-4 sm:space-y-0">
          <div>
            <h1 className="text-3xl font-bold text-primary">Gestão de Clientes</h1>
            <p className="text-muted-foreground mt-1">
              Gerencie os clientes do sistema de aluguel de carros
            </p>
          </div>
          <Button 
            onClick={handleNovoCliente}
            className="bg-gradient-primary hover:opacity-90 shadow-medium"
          >
            <Plus className="h-4 w-4 mr-2" />
            Novo Cliente
          </Button>
        </div>

        {/* Cards de Estatísticas */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="shadow-soft hover:shadow-medium transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total de Clientes</CardTitle>
              <Users className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-primary">{totalClientes}</div>
              <p className="text-xs text-muted-foreground">
                Clientes cadastrados no sistema
              </p>
            </CardContent>
          </Card>

          <Card className="shadow-soft hover:shadow-medium transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Gasto Total</CardTitle>
              <DollarSign className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-accent">{formatarMoeda(gastoTotal)}</div>
              <p className="text-xs text-muted-foreground">
                Soma de todos os gastos com carros
              </p>
            </CardContent>
          </Card>

          <Card className="shadow-soft hover:shadow-medium transition-shadow">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Gasto Médio</CardTitle>
              <TrendingUp className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-accent">{formatarMoeda(gastoMedio)}</div>
              <p className="text-xs text-muted-foreground">
                Média por cliente
              </p>
            </CardContent>
          </Card>
        </div>

        {/* Filtros e Busca */}
        <Card className="shadow-soft">
          <CardContent className="pt-6">
            <div className="flex flex-col sm:flex-row gap-4">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Buscar por nome, CPF, email ou profissão..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
              <Button variant="outline" className="shrink-0">
                <Filter className="h-4 w-4 mr-2" />
                Filtros
              </Button>
            </div>
          </CardContent>
        </Card>

        {/* Tabela de Clientes */}
        <ClienteTable
          clientes={filteredClientes}
          onEdit={handleEditarCliente}
          onView={handleVisualizarCliente}
          onClienteDeleted={carregarClientes}
        />

        {/* Modal de Visualização */}
        <ClienteModal
          cliente={clienteVisualizacao}
          open={!!clienteVisualizacao}
          onClose={() => setClienteVisualizacao(null)}
        />
      </div>
    </div>
  );
}