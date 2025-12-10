package com.importau.fornecedores.repository;

import com.importau.fornecedores.domain.Fornecedor;
import com.importau.fornecedores.domain.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de dados de Fornecedor
 * Implementa padrão Repository do DDD
 */
@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, String> {

    /**
     * Busca fornecedores por país de origem
     */
    List<Fornecedor> findByPaisOrigemIgnoreCase(String paisOrigem);

    /**
     * Busca fornecedores por categoria
     */
    Page<Fornecedor> findByCategoriaIgnoreCase(String categoria, Pageable pageable);

    /**
     * Busca fornecedores por rating
     */
    List<Fornecedor> findByRating(Rating rating);

    /**
     * Busca fornecedores ativos
     */
    List<Fornecedor> findByAtivoTrue();

    /**
     * Busca fornecedores por CNPJ
     */
    List<Fornecedor> findByCnpjContaining(String cnpj);

    /**
     * Busca fornecedores por razão social
     */
    @Query("SELECT f FROM Fornecedor f WHERE LOWER(f.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))")
    List<Fornecedor> findByRazaoSocialContaining(@Param("razaoSocial") String razaoSocial);

    /**
     * Busca fornecedores com parceria de longo prazo
     */
    @Query("SELECT f FROM Fornecedor f WHERE f.tempoParceriaAnos >= :anos")
    List<Fornecedor> findByTempoParceriaAnos(@Param("anos") Integer anos);

    /**
     * Busca fornecedores por moeda de negociação
     */
    List<Fornecedor> findByMoedaNegociacao(String moeda);

    /**
     * Conta fornecedores por categoria
     */
    long countByCategoria(String categoria);

    /**
     * Conta fornecedores por rating
     */
    long countByRating(Rating rating);
}
