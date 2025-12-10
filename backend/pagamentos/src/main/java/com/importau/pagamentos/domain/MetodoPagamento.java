package com.importau.pagamentos.domain;

/**
 * Enum representando os métodos de pagamento disponíveis
 */
public enum MetodoPagamento {
    BOLETO("Boleto Bancário"),
    TRANSFERENCIA("Transferência Bancária"),
    CARTAO_CREDITO("Cartão de Crédito"),
    PIX("PIX"),
    DEBITO_AUTOMATICO("Débito Automático");

    private final String descricao;

    MetodoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
