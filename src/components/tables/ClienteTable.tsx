import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { 
  Table, 
  TableBody, 
  TableCell, 
  TableHead, 
  TableHeader, 
  TableRow 
} from '@/components/ui/table';
import { 
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from '@/components/ui/alert-dialog';
import { Edit, Trash2, Eye, DollarSign, User } from 'lucide-react';
import { Cliente } from '@/types/models';
import { clienteService } from '@/services/clienteService';
import { toast } from '@/hooks/use-toast';

interface ClienteTableProps {
  clientes: Cliente[];
  onEdit: (cliente: Cliente) => void;
  onView: (cliente: Cliente) => void;
  onClienteDeleted: () => void;
}

export function ClienteTable({ clientes, onEdit, onView, onClienteDeleted }: ClienteTableProps) {
  const [clienteParaExcluir, setClienteParaExcluir] = useState<Cliente | null>(null);
  const [excluindo, setExcluindo] = useState(false);

  const handleExcluir = async () => {
    if (!clienteParaExcluir) return;
    
    setExcluindo(true);
    try {
      await clienteService.excluirCliente(clienteParaExcluir.id);
      toast({
        title: 'Cliente excluído',
        description: `${clienteParaExcluir.nome} foi removido com sucesso.`
      });
      onClienteDeleted();
    } catch (error) {
      toast({
        variant: 'destructive',
        title: 'Erro ao excluir',
        description: error instanceof Error ? error.message : 'Erro desconhecido'
      });
    } finally {
      setExcluindo(false);
      setClienteParaExcluir(null);
    }
  };

  const calcularGastoTotal = (cliente: Cliente): number => {
    return cliente.carrosAlugados.reduce((total, c) => total + c.valor, 0);
  };

  const formatarMoeda = (valor: number): string => {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  };

  if (clientes.length === 0) {
    return (
      <Card className="shadow-medium">
        <CardContent className="flex flex-col items-center justify-center py-12">
          <User className="h-12 w-12 text-muted-foreground mb-4" />
          <h3 className="text-lg font-semibold text-muted-foreground">Nenhum cliente encontrado</h3>
          <p className="text-sm text-muted-foreground mt-2">
            Cadastre o primeiro cliente para começar.
          </p>
        </CardContent>
      </Card>
    );
  }

  return (
    <>
      <Card className="shadow-medium">
        <CardHeader className="bg-gradient-secondary">
          <CardTitle className="text-primary flex items-center space-x-2">
            <User className="h-5 w-5" />
            <span>Clientes Cadastrados ({clientes.length})</span>
          </CardTitle>
        </CardHeader>
        <CardContent className="p-0">
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow className="bg-muted/50">
                  <TableHead>Cliente</TableHead>
                  <TableHead>Documentos</TableHead>
                  <TableHead>Contato</TableHead>
                  <TableHead>Profissão</TableHead>
                  <TableHead>Gasto Total</TableHead>
                  <TableHead className="text-right">Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {clientes.map((cliente) => (
                  <TableRow key={cliente.id} className="hover:bg-muted/30">
                    <TableCell>
                      <div>
                        <p className="font-medium text-foreground">{cliente.nome}</p>
                        <p className="text-sm text-muted-foreground">ID: {cliente.id}</p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div className="space-y-1">
                        <Badge variant="outline" className="text-xs">
                          CPF: {cliente.cpf}
                        </Badge>
                        <br />
                        <Badge variant="outline" className="text-xs">
                          RG: {cliente.rg}
                        </Badge>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div>
                        <p className="text-sm">{cliente.login}</p>
                        <p className="text-xs text-muted-foreground truncate max-w-[200px]">
                          {cliente.endereco}
                        </p>
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="secondary">
                        {cliente.profissao}
                      </Badge>
                    </TableCell>
                    <TableCell>
                      <div className="flex items-center space-x-1">
                        <DollarSign className="h-4 w-4 text-accent" />
                        <span className="font-medium text-accent">
                          {formatarMoeda(calcularGastoTotal(cliente))}
                        </span>
                      </div>
                      <p className="text-xs text-muted-foreground">
                        {cliente.carrosAlugados.length} carro(s)
                      </p>
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end space-x-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => onView(cliente)}
                          className="h-8 w-8 p-0"
                        >
                          <Eye className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => onEdit(cliente)}
                          className="h-8 w-8 p-0"
                        >
                          <Edit className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => setClienteParaExcluir(cliente)}
                          className="h-8 w-8 p-0 text-destructive hover:text-destructive"
                        >
                          <Trash2 className="h-4 w-4" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>

      {/* Dialog de Confirmação */}
      <AlertDialog open={!!clienteParaExcluir} onOpenChange={() => setClienteParaExcluir(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Confirmar Exclusão</AlertDialogTitle>
            <AlertDialogDescription>
              Tem certeza de que deseja excluir o cliente <strong>{clienteParaExcluir?.nome}</strong>?
              Esta ação não pode ser desfeita.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={excluindo}>
              Cancelar
            </AlertDialogCancel>
            <AlertDialogAction
              onClick={handleExcluir}
              disabled={excluindo}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              {excluindo ? 'Excluindo...' : 'Excluir'}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  );
}