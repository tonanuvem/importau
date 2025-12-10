package com.importau.pagamentos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um pagamento
 * Contém informações sobre transações financeiras relacionadas a pedidos
 */
@Entity
@Table(name = "pagamentos")
public class Pagamento {

    @Id
    @Column(name = "pagamento_id")
    private String pagamentoId;

    @NotNull
    @Column(name = "pedido_id")
    private String pedidoId;

    @NotNull
    @Column(name = "fornecedor_id")
    private String fornecedorId;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @NotNull
    @Positive
    @Column(name = "valor_pago_brl", precision = 15, scale = 2)
    private BigDecimal valorPagoBrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento")
    private StatusPagamento statusPagamento;

    @Column(name = "taxa_cambio_aplicada", precision = 10, scale = 4)
    private BigDecimal taxaCambioAplicada;

    @Column(name = "moeda_origem", length = 3)
    private String moedaOrigem;

    @Column(name = "valor_moeda_origem", precision = 15, scale = 2)
    private BigDecimal valorMoedaOrigem;

    @Column(name = "num_parcelas")
    private Integer numParcelas;

    @Column(name = "parcela_atual")
    private Integer parcelaAtual;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "usuario_aprovacao")
    private String usuarioAprovacao;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Pagamento() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Pagamento(String pagamentoId, String pedidoId, String fornecedorId) {
        this();
        this.pagamentoId = pagamentoId;
        this.pedidoId = pedidoId;
        this.fornecedorId = fornecedorId;
    }

    // Métodos de negócio
    public boolean isPago() {
        return StatusPagamento.PAGO.equals(this.statusPagamento);
    }

    public boolean isPendente() {
        return StatusPagamento.PENDENTE.equals(this.statusPagamento);
    }

    public boolean isParcelado() {
        return this.numParcelas != null && this.numParcelas > 1;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public String getPagamentoId() { return pagamentoId; }
    public void setPagamentoId(String pagamentoId) { this.pagamentoId = pagamentoId; }

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }

    public String getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(String fornecedorId) { this.fornecedorId = fornecedorId; }

    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }

    public BigDecimal getValorPagoBrl() { return valorPagoBrl; }
    public void setValorPagoBrl(BigDecimal valorPagoBrl) { this.valorPagoBrl = valorPagoBrl; }

    public MetodoPagamento getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(MetodoPagamento metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public StatusPagamento getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(StatusPagamento statusPagamento) { this.statusPagamento = statusPagamento; }

    public BigDecimal getTaxaCambioAplicada() { return taxaCambioAplicada; }
    public void setTaxaCambioAplicada(BigDecimal taxaCambioAplicada) { this.taxaCambioAplicada = taxaCambioAplicada; }

    public String getMoedaOrigem() { return moedaOrigem; }
    public void setMoedaOrigem(String moedaOrigem) { this.moedaOrigem = moedaOrigem; }

    public BigDecimal getValorMoedaOrigem() { return valorMoedaOrigem; }
    public void setValorMoedaOrigem(BigDecimal valorMoedaOrigem) { this.valorMoedaOrigem = valorMoedaOrigem; }

    public Integer getNumParcelas() { return numParcelas; }
    public void setNumParcelas(Integer numParcelas) { this.numParcelas = numParcelas; }

    public Integer getParcelaAtual() { return parcelaAtual; }
    public void setParcelaAtual(Integer parcelaAtual) { this.parcelaAtual = parcelaAtual; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public String getUsuarioAprovacao() { return usuarioAprovacao; }
    public void setUsuarioAprovacao(String usuarioAprovacao) { this.usuarioAprovacao = usuarioAprovacao; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
