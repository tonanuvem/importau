package com.importau.pagamentos.repository;

import com.importau.pagamentos.domain.Pagamento;
import com.importau.pagamentos.domain.StatusPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operações de dados de Pagamento
 * Implementa padrão Repository do DDD
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {

    /**
     * Busca pagamentos por pedido
     */
    List<Pagamento> findByPedidoId(String pedidoId);

    /**
     * Busca pagamentos por fornecedor
     */
    Page<Pagamento> findByFornecedorId(String fornecedorId, Pageable pageable);

    /**
     * Busca pagamentos por status
     */
    List<Pagamento> findByStatusPagamento(StatusPagamento status);

    /**
     * Busca pagamentos por período
     */
    @Query("SELECT p FROM Pagamento p WHERE p.dataPagamento BETWEEN :dataInicio AND :dataFim")
    List<Pagamento> findByPeriodo(@Param("dataInicio") LocalDate dataInicio, 
                                  @Param("dataFim") LocalDate dataFim);

    /**
     * Busca pagamentos pendentes com vencimento próximo
     */
    @Query("SELECT p FROM Pagamento p WHERE p.statusPagamento = 'PENDENTE' " +
           "AND p.dataVencimento BETWEEN :hoje AND :dataLimite")
    List<Pagamento> findPagamentosPendentesVencimento(@Param("hoje") LocalDate hoje,
                                                      @Param("dataLimite") LocalDate dataLimite);

    /**
     * Conta pagamentos por status
     */
    long countByStatusPagamento(StatusPagamento status);
}
