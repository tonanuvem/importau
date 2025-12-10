package com.importau.pagamentos.service;

import com.importau.pagamentos.domain.Pagamento;
import com.importau.pagamentos.domain.StatusPagamento;
import com.importau.pagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serviço de domínio para Pagamentos
 * Implementa regras de negócio e coordena operações
 */
@Service
@Transactional
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    /**
     * Busca todos os pagamentos com paginação
     */
    @Transactional(readOnly = true)
    public Page<Pagamento> findAll(Pageable pageable) {
        return pagamentoRepository.findAll(pageable);
    }

    /**
     * Busca pagamento por ID
     */
    @Transactional(readOnly = true)
    public Optional<Pagamento> findById(String id) {
        return pagamentoRepository.findById(id);
    }

    /**
     * Cria novo pagamento
     */
    public Pagamento create(Pagamento pagamento) {
        // Validações de negócio
        if (pagamento.getStatusPagamento() == null) {
            pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        }
        
        return pagamentoRepository.save(pagamento);
    }

    /**
     * Atualiza pagamento existente
     */
    public Pagamento update(String id, Pagamento pagamentoAtualizado) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                // Atualiza apenas campos permitidos
                pagamento.setDataPagamento(pagamentoAtualizado.getDataPagamento());
                pagamento.setValorPagoBrl(pagamentoAtualizado.getValorPagoBrl());
                pagamento.setMetodoPagamento(pagamentoAtualizado.getMetodoPagamento());
                pagamento.setStatusPagamento(pagamentoAtualizado.getStatusPagamento());
                pagamento.setDataVencimento(pagamentoAtualizado.getDataVencimento());
                pagamento.setUsuarioAprovacao(pagamentoAtualizado.getUsuarioAprovacao());
                
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
    }

    /**
     * Remove pagamento
     */
    public void delete(String id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new RuntimeException("Pagamento não encontrado: " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    /**
     * Busca pagamentos por pedido
     */
    @Transactional(readOnly = true)
    public List<Pagamento> findByPedidoId(String pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId);
    }

    /**
     * Busca pagamentos por fornecedor
     */
    @Transactional(readOnly = true)
    public Page<Pagamento> findByFornecedorId(String fornecedorId, Pageable pageable) {
        return pagamentoRepository.findByFornecedorId(fornecedorId, pageable);
    }

    /**
     * Busca pagamentos por status
     */
    @Transactional(readOnly = true)
    public List<Pagamento> findByStatus(StatusPagamento status) {
        return pagamentoRepository.findByStatusPagamento(status);
    }

    /**
     * Busca pagamentos por período
     */
    @Transactional(readOnly = true)
    public List<Pagamento> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return pagamentoRepository.findByPeriodo(dataInicio, dataFim);
    }

    /**
     * Confirma pagamento (altera status para PAGO)
     */
    public Pagamento confirmarPagamento(String id, String usuarioAprovacao) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                pagamento.setStatusPagamento(StatusPagamento.PAGO);
                pagamento.setDataPagamento(LocalDate.now());
                pagamento.setUsuarioAprovacao(usuarioAprovacao);
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
    }

    /**
     * Cancela pagamento
     */
    public Pagamento cancelarPagamento(String id, String usuarioAprovacao) {
        return pagamentoRepository.findById(id)
            .map(pagamento -> {
                if (pagamento.isPago()) {
                    throw new RuntimeException("Não é possível cancelar pagamento já realizado");
                }
                pagamento.setStatusPagamento(StatusPagamento.CANCELADO);
                pagamento.setUsuarioAprovacao(usuarioAprovacao);
                return pagamentoRepository.save(pagamento);
            })
            .orElseThrow(() -> new RuntimeException("Pagamento não encontrado: " + id));
    }

    /**
     * Busca pagamentos com vencimento próximo
     */
    @Transactional(readOnly = true)
    public List<Pagamento> findPagamentosVencimentoProximo(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(dias);
        return pagamentoRepository.findPagamentosPendentesVencimento(hoje, dataLimite);
    }
}
