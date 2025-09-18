import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Badge } from '@/components/ui/badge';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { User, Mail, MapPin, Briefcase, DollarSign, FileText } from 'lucide-react';
import { Cliente } from '@/types/models';

interface ClienteModalProps {
  cliente: Cliente | null;
  open: boolean;
  onClose: () => void;
}

export function ClienteModal({ cliente, open, onClose }: ClienteModalProps) {
  if (!cliente) return null;

  const formatarMoeda = (valor: number): string => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  };

  const calcularTotalGasto = (): number => {
    return cliente.carrosAlugados.reduce((total, c) => total + c.valor, 0);
  };

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle className="flex items-center space-x-2">
            <User className="h-5 w-5 text-primary" />
            <span>Detalhes do Cliente</span>
          </DialogTitle>
        </DialogHeader>

        <div className="space-y-6">
          {/* Informações Pessoais */}
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-lg flex items-center space-x-2">
                <User className="h-4 w-4" />
                <span>Informações Pessoais</span>
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Nome</p>
                  <p className="text-base">{cliente.nome}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">Profissão</p>
                  <Badge variant="secondary" className="mt-1">
                    <Briefcase className="h-3 w-3 mr-1" />
                    {cliente.profissao}
                  </Badge>
                </div>
              </div>

              <Separator />

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p className="text-sm font-medium text-muted-foreground">CPF</p>
                  <p className="text-base font-mono">{cliente.cpf}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-muted-foreground">RG</p>
                  <p className="text-base font-mono">{cliente.rg}</p>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Contato */}
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-lg flex items-center space-x-2">
                <Mail className="h-4 w-4" />
                <span>Contato</span>
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <p className="text-sm font-medium text-muted-foreground">Email</p>
                <p className="text-base">{cliente.login}</p>
              </div>
              
              <Separator />

              <div>
                <p className="text-sm font-medium text-muted-foreground flex items-center space-x-1">
                  <MapPin className="h-3 w-3" />
                  <span>Endereço</span>
                </p>
                <p className="text-base mt-1">{cliente.endereco}</p>
              </div>
            </CardContent>
          </Card>

          {/* Carros Alugados */}
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-lg flex items-center space-x-2">
                <DollarSign className="h-4 w-4" />
                <span>Histórico de Carros Alugados</span>
                <Badge variant="outline" className="ml-auto">
                  Total Gasto: {formatarMoeda(calcularTotalGasto())}
                </Badge>
              </CardTitle>
            </CardHeader>
            <CardContent>
              {cliente.carrosAlugados.length > 0 ? (
                <div className="space-y-3">
                  {cliente.carrosAlugados.map((carro, index) => (
                    <div key={carro.id} className="p-3 bg-muted/50 rounded-lg">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center space-x-2">
                          <FileText className="h-4 w-4 text-muted-foreground" />
                          <div>
                            <p className="text-sm font-medium">{carro.marca} {carro.modelo}</p>
                            <p className="text-xs text-muted-foreground">Placa: {carro.placa}</p>
                          </div>
                        </div>
                        <Badge variant="outline" className="font-mono">
                          {formatarMoeda(carro.valor)}
                        </Badge>
                      </div>
                      <div className="mt-2 text-xs text-muted-foreground">
                        {new Date(carro.dataInicio).toLocaleDateString('pt-BR')} até {new Date(carro.dataFim).toLocaleDateString('pt-BR')}
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-sm text-muted-foreground text-center py-4">
                  Nenhum carro alugado ainda
                </p>
              )}
            </CardContent>
          </Card>

          {/* Informações do Sistema */}
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="text-lg">Informações do Sistema</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <div className="flex justify-between">
                <span className="text-sm text-muted-foreground">ID do Cliente:</span>
                <Badge variant="outline">{cliente.id}</Badge>
              </div>
              <div className="flex justify-between">
                <span className="text-sm text-muted-foreground">Perfil:</span>
                <Badge variant="secondary" className="capitalize">
                  {cliente.perfil.toLowerCase()}
                </Badge>
              </div>
            </CardContent>
          </Card>
        </div>
      </DialogContent>
    </Dialog>
  );
}