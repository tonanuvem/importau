package com.importau.fornecedores.domain;

/**
 * Enum representando os ratings de qualidade dos fornecedores
 */
public enum Rating {
    A_PLUS("A+", "Excelente"),
    A("A", "Muito Bom"),
    A_MINUS("A-", "Bom"),
    B_PLUS("B+", "Satisfatório"),
    B("B", "Regular"),
    B_MINUS("B-", "Abaixo da Média"),
    C("C", "Insatisfatório");

    private final String codigo;
    private final String descricao;

    Rating(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Rating fromCodigo(String codigo) {
        for (Rating rating : values()) {
            if (rating.codigo.equals(codigo)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Rating inválido: " + codigo);
    }
}
