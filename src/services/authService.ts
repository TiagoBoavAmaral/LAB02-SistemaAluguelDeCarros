import { Usuario, Cliente } from '@/types/models';
import { clienteService } from './clienteService';

export interface AuthState {
  usuario: Usuario | null;
  isAuthenticated: boolean;
  loading: boolean;
}

class AuthService {
  private currentUser: Usuario | null = null;

  private delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

  async login(email: string, senha: string): Promise<Usuario> {
    await this.delay(1000);

    // Buscar nas listas de usuários (clientes e agentes)
    const clientes = await clienteService.listarClientes();
    const usuario = clientes.find(c => c.login === email && c.senha === senha);

    if (!usuario) {
      throw new Error('Email ou senha inválidos');
    }

    this.currentUser = usuario;
    localStorage.setItem('currentUser', JSON.stringify(usuario));
    
    return usuario;
  }

  async logout(): Promise<void> {
    await this.delay(300);
    this.currentUser = null;
    localStorage.removeItem('currentUser');
  }

  getCurrentUser(): Usuario | null {
    if (this.currentUser) {
      return this.currentUser;
    }

    // Tentar recuperar do localStorage
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUser = JSON.parse(storedUser);
      return this.currentUser;
    }

    return null;
  }

  isAuthenticated(): boolean {
    return this.getCurrentUser() !== null;
  }

  hasPermission(action: string): boolean {
    const user = this.getCurrentUser();
    if (!user) return false;

    // Por enquanto, apenas verificação simples de perfil
    switch (action) {
      case 'create_cliente':
      case 'update_cliente':
      case 'delete_cliente':
        return user.perfil === 'AGENTE';
      case 'view_clientes':
        return ['CLIENTE', 'AGENTE'].includes(user.perfil);
      default:
        return false;
    }
  }
}

export const authService = new AuthService();