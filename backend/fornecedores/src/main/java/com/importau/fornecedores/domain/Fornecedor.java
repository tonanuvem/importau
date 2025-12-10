package com.importau.fornecedores.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entidade de domínio representando um fornecedor
 * Contém informações sobre empresas fornecedoras de produtos/serviços
 */
@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "fornecedor_id")
    private String fornecedorId;

    @NotBlank
    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "cnpj")
    private String cnpj;

    @NotBlank
    @Column(name = "pais_origem", nullable = false)
    private String paisOrigem;

    @NotBlank
    @Column(name = "categoria", nullable = false)
    private String categoria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @Column(name = "tempo_parceria_anos")
    private Integer tempoParceriaAnos;

    @Column(name = "condicoes_pagamento")
    private String condicoesPagamento;

    @Column(name = "moeda_negociacao", length = 3)
    private String moedaNegociacao;

    @Email
    @Column(name = "contato_email")
    private String contatoEmail;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "ativo")
    private Boolean ativo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Construtores
    public Fornecedor() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.ativo = true;
    }

    public Fornecedor(String fornecedorId, String razaoSocial, String paisOrigem, String categoria) {
        this();
        this.fornecedorId = fornecedorId;
        this.razaoSocial = razaoSocial;
        this.paisOrigem = paisOrigem;
        this.categoria = categoria;
    }

    // Métodos de negócio
    public boolean isAtivo() {
        return Boolean.TRUE.equals(this.ativo);
    }

    public boolean isFornecedorInternacional() {
        return !"Brasil".equalsIgnoreCase(this.paisOrigem);
    }

    public boolean isParceriaLongaPrazo() {
        return this.tempoParceriaAnos != null && this.tempoParceriaAnos >= 5;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public String getFornecedorId() { return fornecedorId; }
    public void setFornecedorId(String fornecedorId) { this.fornecedorId = fornecedorId; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getPaisOrigem() { return paisOrigem; }
    public void setPaisOrigem(String paisOrigem) { this.paisOrigem = paisOrigem; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Rating getRating() { return rating; }
    public void setRating(Rating rating) { this.rating = rating; }

    public Integer getTempoParceriaAnos() { return tempoParceriaAnos; }
    public void setTempoParceriaAnos(Integer tempoParceriaAnos) { this.tempoParceriaAnos = tempoParceriaAnos; }

    public String getCondicoesPagamento() { return condicoesPagamento; }
    public void setCondicoesPagamento(String condicoesPagamento) { this.condicoesPagamento = condicoesPagamento; }

    public String getMoedaNegociacao() { return moedaNegociacao; }
    public void setMoedaNegociacao(String moedaNegociacao) { this.moedaNegociacao = moedaNegociacao; }

    public String getContatoEmail() { return contatoEmail; }
    public void setContatoEmail(String contatoEmail) { this.contatoEmail = contatoEmail; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
