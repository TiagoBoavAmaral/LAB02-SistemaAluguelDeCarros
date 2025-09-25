package com.pucminas.rental_system.service;

import com.pucminas.rental_system.model.*;
import com.pucminas.rental_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoAluguelService {
    @Autowired private PedidoAluguelRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private AutomovelRepository automovelRepository;
    @Autowired private AgenteRepository agenteRepository;

    public PedidoAluguel criarPedido(Long clienteId, Long automovelId) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        Automovel automovel = automovelRepository.findById(automovelId).orElseThrow(() -> new RuntimeException("Automóvel não encontrado"));

        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(PedidoAluguel.PedidoStatus.PENDENTE);
        
        return pedidoRepository.save(pedido);
    }
    
    public PedidoAluguel avaliarPedido(Long pedidoId, Long agenteId, boolean aprovar) {
        PedidoAluguel pedido = pedidoRepository.findById(pedidoId).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Agente agente = agenteRepository.findById(agenteId).orElseThrow(() -> new RuntimeException("Agente não encontrado"));
        
        pedido.setAgente(agente);
        pedido.setStatus(aprovar ? PedidoAluguel.PedidoStatus.APROVADO : PedidoAluguel.PedidoStatus.REJEITADO);
        
        return pedidoRepository.save(pedido);
    }
    
    public List<PedidoAluguel> findPedidosPorCliente(Cliente cliente) {
        return pedidoRepository.findByCliente(cliente);
    }
    
    public List<PedidoAluguel> findPedidosPendentes() {
        return pedidoRepository.findByStatus(PedidoAluguel.PedidoStatus.PENDENTE);
    }
}
