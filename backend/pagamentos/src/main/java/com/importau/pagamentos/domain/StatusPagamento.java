package com.importau.pagamentos.domain;

/**
 * Enum representando os status possíveis de um pagamento
 */
public enum StatusPagamento {
    PENDENTE("Pendente de Pagamento"),
    PAGO("Pago"),
    CANCELADO("Cancelado"),
    AGENDADO("Agendado"),
    PROCESSANDO("Processando"),
    REJEITADO("Rejeitado");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
