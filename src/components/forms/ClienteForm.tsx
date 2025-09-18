import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Plus, Minus, Save, X } from 'lucide-react';
import { Cliente, CarroAlugado, FormError } from '@/types/models';
import { clienteService } from '@/services/clienteService';
import { toast } from '@/hooks/use-toast';

interface ClienteFormProps {
  cliente?: Cliente;
  onSave: (cliente: Cliente) => void;
  onCancel: () => void;
}

export function ClienteForm({ cliente, onSave, onCancel }: ClienteFormProps) {
  const [formData, setFormData] = useState<Partial<Cliente>>({
    nome: '',
    cpf: '',
    rg: '',
    login: '',
    senha: '',
    endereco: '',
    profissao: '',
    perfil: 'CLIENTE',
    carrosAlugados: []
  });
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<FormError[]>([]);

  useEffect(() => {
    if (cliente) {
      setFormData({
        ...cliente,
        carrosAlugados: cliente.carrosAlugados || []
      });
    }
  }, [cliente]);

  const formatCPF = (cpf: string) => {
    return cpf
      .replace(/\D/g, '')
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d{1,2})/, '$1-$2')
      .replace(/(-\d{2})\d+?$/, '$1');
  };

  const handleInputChange = (field: keyof Cliente, value: string) => {
    if (field === 'cpf') {
      value = formatCPF(value);
    }
    
    setFormData(prev => ({ ...prev, [field]: value }));
    
    // Limpar erro do campo específico
    setErrors(prev => prev.filter(e => e.field !== field));
  };

  const handleCarroAlugadoChange = (index: number, field: keyof CarroAlugado, value: string | number | Date) => {
    const carrosAlugados = [...(formData.carrosAlugados || [])];
    carrosAlugados[index] = { 
      ...carrosAlugados[index], 
      [field]: field === 'valor' ? parseFloat(value as string) || 0 : 
               field === 'dataInicio' || field === 'dataFim' ? new Date(value as string) : value 
    };
    setFormData(prev => ({ ...prev, carrosAlugados }));
  };

  const addCarroAlugado = () => {
    const carrosAlugados = [...(formData.carrosAlugados || [])];
    if (carrosAlugados.length < 5) {
      carrosAlugados.push({ 
        id: 0, 
        marca: '', 
        modelo: '', 
        placa: '', 
        dataInicio: new Date(), 
        dataFim: new Date(), 
        valor: 0 
      });
      setFormData(prev => ({ ...prev, carrosAlugados }));
    }
  };

  const removeCarroAlugado = (index: number) => {
    const carrosAlugados = [...(formData.carrosAlugados || [])];
    if (carrosAlugados.length > 0) {
      carrosAlugados.splice(index, 1);
      setFormData(prev => ({ ...prev, carrosAlugados }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrors([]);

    try {
      // Validar dados
      const validationErrors = clienteService.validarDadosCliente(formData);
      if (validationErrors.length > 0) {
        setErrors(validationErrors);
        return;
      }

      // Filtrar carros alugados vazios
      const dadosLimpos = {
        ...formData,
        carrosAlugados: (formData.carrosAlugados || []).filter(c => c.marca && c.modelo && c.placa)
      } as Cliente;

      let clienteSalvo: Cliente;
      if (cliente?.id) {
        clienteSalvo = await clienteService.atualizarCliente(cliente.id, dadosLimpos);
        toast({
          title: 'Cliente atualizado',
          description: 'Os dados foram salvos com sucesso.'
        });
      } else {
        clienteSalvo = await clienteService.criarCliente(dadosLimpos);
        toast({
          title: 'Cliente cadastrado',
          description: 'Novo cliente foi criado com sucesso.'
        });
      }

      onSave(clienteSalvo);
    } catch (error) {
      toast({
        variant: 'destructive',
        title: 'Erro ao salvar',
        description: error instanceof Error ? error.message : 'Erro desconhecido'
      });
    } finally {
      setLoading(false);
    }
  };

  const getFieldError = (field: string) => {
    return errors.find(e => e.field === field)?.message;
  };

  return (
    <form onSubmit={handleSubmit}>
      <Card className="shadow-medium">
        <CardHeader className="bg-gradient-secondary">
          <CardTitle className="text-primary">
            {cliente?.id ? 'Editar Cliente' : 'Novo Cliente'}
          </CardTitle>
        </CardHeader>

        <CardContent className="space-y-6 p-6">
          {/* Dados Pessoais */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="nome">Nome Completo*</Label>
              <Input
                id="nome"
                value={formData.nome || ''}
                onChange={(e) => handleInputChange('nome', e.target.value)}
                placeholder="Digite o nome completo"
                className={getFieldError('nome') ? 'border-destructive' : ''}
              />
              {getFieldError('nome') && (
                <p className="text-sm text-destructive">{getFieldError('nome')}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="cpf">CPF*</Label>
              <Input
                id="cpf"
                value={formData.cpf || ''}
                onChange={(e) => handleInputChange('cpf', e.target.value)}
                placeholder="000.000.000-00"
                maxLength={14}
                className={getFieldError('cpf') ? 'border-destructive' : ''}
              />
              {getFieldError('cpf') && (
                <p className="text-sm text-destructive">{getFieldError('cpf')}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="rg">RG*</Label>
              <Input
                id="rg"
                value={formData.rg || ''}
                onChange={(e) => handleInputChange('rg', e.target.value)}
                placeholder="00.000.000-0"
                className={getFieldError('rg') ? 'border-destructive' : ''}
              />
              {getFieldError('rg') && (
                <p className="text-sm text-destructive">{getFieldError('rg')}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="profissao">Profissão*</Label>
              <Input
                id="profissao"
                value={formData.profissao || ''}
                onChange={(e) => handleInputChange('profissao', e.target.value)}
                placeholder="Digite a profissão"
                className={getFieldError('profissao') ? 'border-destructive' : ''}
              />
              {getFieldError('profissao') && (
                <p className="text-sm text-destructive">{getFieldError('profissao')}</p>
              )}
            </div>
          </div>

          {/* Endereço */}
          <div className="space-y-2">
            <Label htmlFor="endereco">Endereço Completo*</Label>
            <Input
              id="endereco"
              value={formData.endereco || ''}
              onChange={(e) => handleInputChange('endereco', e.target.value)}
              placeholder="Rua, número, bairro, cidade, estado"
              className={getFieldError('endereco') ? 'border-destructive' : ''}
            />
            {getFieldError('endereco') && (
              <p className="text-sm text-destructive">{getFieldError('endereco')}</p>
            )}
          </div>

          {/* Dados de Acesso */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="login">Email*</Label>
              <Input
                id="login"
                type="email"
                value={formData.login || ''}
                onChange={(e) => handleInputChange('login', e.target.value)}
                placeholder="email@exemplo.com"
                className={getFieldError('login') ? 'border-destructive' : ''}
              />
              {getFieldError('login') && (
                <p className="text-sm text-destructive">{getFieldError('login')}</p>
              )}
            </div>

            {!cliente?.id && (
              <div className="space-y-2">
                <Label htmlFor="senha">Senha*</Label>
                <Input
                  id="senha"
                  type="password"
                  value={formData.senha || ''}
                  onChange={(e) => handleInputChange('senha', e.target.value)}
                  placeholder="Digite a senha"
                  className={getFieldError('senha') ? 'border-destructive' : ''}
                />
                {getFieldError('senha') && (
                  <p className="text-sm text-destructive">{getFieldError('senha')}</p>
                )}
              </div>
            )}
          </div>

          {/* Carros Alugados */}
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <Label>Histórico de Carros Alugados (máximo 5)</Label>
              {(formData.carrosAlugados?.length || 0) < 5 && (
                <Button 
                  type="button" 
                  variant="outline" 
                  size="sm"
                  onClick={addCarroAlugado}
                >
                  <Plus className="h-4 w-4 mr-1" />
                  Adicionar
                </Button>
              )}
            </div>

            {formData.carrosAlugados?.map((carro, index) => (
              <div key={index} className="grid grid-cols-1 md:grid-cols-3 gap-2 p-3 border rounded-lg">
                <div>
                  <Input
                    placeholder="Marca"
                    value={carro.marca}
                    onChange={(e) => handleCarroAlugadoChange(index, 'marca', e.target.value)}
                  />
                </div>
                <div>
                  <Input
                    placeholder="Modelo"
                    value={carro.modelo}
                    onChange={(e) => handleCarroAlugadoChange(index, 'modelo', e.target.value)}
                  />
                </div>
                <div className="flex gap-2">
                  <Input
                    placeholder="Placa"
                    value={carro.placa}
                    onChange={(e) => handleCarroAlugadoChange(index, 'placa', e.target.value)}
                  />
                  {(formData.carrosAlugados?.length || 0) > 0 && (
                    <Button
                      type="button"
                      variant="outline"
                      size="sm"
                      onClick={() => removeCarroAlugado(index)}
                    >
                      <Minus className="h-4 w-4" />
                    </Button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </CardContent>

        <CardFooter className="flex justify-end space-x-2 bg-secondary">
          <Button 
            type="button" 
            variant="outline" 
            onClick={onCancel}
            disabled={loading}
          >
            <X className="h-4 w-4 mr-1" />
            Cancelar
          </Button>
          <Button 
            type="submit" 
            disabled={loading}
            className="bg-gradient-primary hover:opacity-90"
          >
            <Save className="h-4 w-4 mr-1" />
            {loading ? 'Salvando...' : 'Salvar'}
          </Button>
        </CardFooter>
      </Card>
    </form>
  );
}