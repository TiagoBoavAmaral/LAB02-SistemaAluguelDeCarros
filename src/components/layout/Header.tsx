import { Button } from '@/components/ui/button';
import { Car, LogOut, User, Users } from 'lucide-react';
import { authService } from '@/services/authService';
import { useNavigate } from 'react-router-dom';
import { toast } from '@/hooks/use-toast';

interface HeaderProps {
  onNavigate?: (page: string) => void;
}

export function Header({ onNavigate }: HeaderProps) {
  const navigate = useNavigate();
  const currentUser = authService.getCurrentUser();

  const handleLogout = async () => {
    try {
      await authService.logout();
      toast({
        title: 'Logout realizado',
        description: 'Você foi desconectado com sucesso.'
      });
      navigate('/');
    } catch (error) {
      toast({
        variant: 'destructive',
        title: 'Erro ao fazer logout',
        description: 'Tente novamente.'
      });
    }
  };

  return (
    <header className="bg-card border-b shadow-soft">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <div className="flex items-center space-x-3">
            <div className="p-2 bg-gradient-primary rounded-lg shadow-glow">
              <Car className="h-6 w-6 text-primary-foreground" />
            </div>
            <div>
              <h1 className="text-xl font-bold text-primary">CarRental</h1>
              <p className="text-xs text-muted-foreground">Sistema de Aluguel</p>
            </div>
          </div>

          {/* Navigation */}
          {currentUser && (
            <nav className="hidden md:flex space-x-1">
              <Button 
                variant="ghost" 
                size="sm"
                onClick={() => onNavigate?.('dashboard')}
                className="flex items-center space-x-2"
              >
                <Users className="h-4 w-4" />
                <span>Clientes</span>
              </Button>
            </nav>
          )}

          {/* User Menu */}
          {currentUser ? (
            <div className="flex items-center space-x-3">
              <div className="flex items-center space-x-2 px-3 py-1.5 bg-secondary rounded-lg">
                <User className="h-4 w-4 text-secondary-foreground" />
                <div className="text-sm">
                  <p className="font-medium text-secondary-foreground">{currentUser.login}</p>
                  <p className="text-xs text-muted-foreground capitalize">{currentUser.perfil.toLowerCase()}</p>
                </div>
              </div>
              <Button 
                variant="outline" 
                size="sm"
                onClick={handleLogout}
                className="flex items-center space-x-1"
              >
                <LogOut className="h-4 w-4" />
                <span className="hidden sm:block">Sair</span>
              </Button>
            </div>
          ) : (
            <Button 
              onClick={() => navigate('/')}
              className="bg-gradient-primary hover:opacity-90"
            >
              Entrar
            </Button>
          )}
        </div>
      </div>
    </header>
  );
}