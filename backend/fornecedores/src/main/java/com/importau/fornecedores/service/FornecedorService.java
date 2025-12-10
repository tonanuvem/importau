package com.importau.fornecedores.service;

import com.importau.fornecedores.domain.Fornecedor;
import com.importau.fornecedores.domain.Rating;
import com.importau.fornecedores.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de domínio para Fornecedores
 * Implementa regras de negócio e coordena operações
 */
@Service
@Transactional
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Autowired
    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    /**
     * Busca todos os fornecedores com paginação
     */
    @Transactional(readOnly = true)
    public Page<Fornecedor> findAll(Pageable pageable) {
        return fornecedorRepository.findAll(pageable);
    }

    /**
     * Busca fornecedor por ID
     */
    @Transactional(readOnly = true)
    public Optional<Fornecedor> findById(String id) {
        return fornecedorRepository.findById(id);
    }

    /**
     * Cria novo fornecedor
     */
    public Fornecedor create(Fornecedor fornecedor) {
        // Validações de negócio
        if (fornecedor.getAtivo() == null) {
            fornecedor.setAtivo(true);
        }
        
        return fornecedorRepository.save(fornecedor);
    }

    /**
     * Atualiza fornecedor existente
     */
    public Fornecedor update(String id, Fornecedor fornecedorAtualizado) {
        return fornecedorRepository.findById(id)
            .map(fornecedor -> {
                // Atualiza apenas campos permitidos
                fornecedor.setRazaoSocial(fornecedorAtualizado.getRazaoSocial());
                fornecedor.setCnpj(fornecedorAtualizado.getCnpj());
                fornecedor.setPaisOrigem(fornecedorAtualizado.getPaisOrigem());
                fornecedor.setCategoria(fornecedorAtualizado.getCategoria());
                fornecedor.setRating(fornecedorAtualizado.getRating());
                fornecedor.setTempoParceriaAnos(fornecedorAtualizado.getTempoParceriaAnos());
                fornecedor.setCondicoesPagamento(fornecedorAtualizado.getCondicoesPagamento());
                fornecedor.setMoedaNegociacao(fornecedorAtualizado.getMoedaNegociacao());
                fornecedor.setContatoEmail(fornecedorAtualizado.getContatoEmail());
                fornecedor.setTelefone(fornecedorAtualizado.getTelefone());
                fornecedor.setAtivo(fornecedorAtualizado.getAtivo());
                
                return fornecedorRepository.save(fornecedor);
            })
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: " + id));
    }

    /**
     * Remove fornecedor (soft delete)
     */
    public void delete(String id) {
        fornecedorRepository.findById(id)
            .map(fornecedor -> {
                fornecedor.setAtivo(false);
                return fornecedorRepository.save(fornecedor);
            })
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: " + id));
    }

    /**
     * Busca fornecedores por país
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> findByPais(String pais) {
        return fornecedorRepository.findByPaisOrigemIgnoreCase(pais);
    }

    /**
     * Busca fornecedores por categoria
     */
    @Transactional(readOnly = true)
    public Page<Fornecedor> findByCategoria(String categoria, Pageable pageable) {
        return fornecedorRepository.findByCategoriaIgnoreCase(categoria, pageable);
    }

    /**
     * Busca fornecedores por rating
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> findByRating(Rating rating) {
        return fornecedorRepository.findByRating(rating);
    }

    /**
     * Busca fornecedores ativos
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> findAtivos() {
        return fornecedorRepository.findByAtivoTrue();
    }

    /**
     * Busca fornecedores por razão social
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> findByRazaoSocial(String razaoSocial) {
        return fornecedorRepository.findByRazaoSocialContaining(razaoSocial);
    }

    /**
     * Ativa fornecedor
     */
    public Fornecedor ativar(String id) {
        return fornecedorRepository.findById(id)
            .map(fornecedor -> {
                fornecedor.setAtivo(true);
                return fornecedorRepository.save(fornecedor);
            })
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: " + id));
    }

    /**
     * Desativa fornecedor
     */
    public Fornecedor desativar(String id) {
        return fornecedorRepository.findById(id)
            .map(fornecedor -> {
                fornecedor.setAtivo(false);
                return fornecedorRepository.save(fornecedor);
            })
            .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado: " + id));
    }

    /**
     * Busca fornecedores com parceria de longo prazo
     */
    @Transactional(readOnly = true)
    public List<Fornecedor> findParceriaLongaPrazo(Integer anos) {
        return fornecedorRepository.findByTempoParceriaAnos(anos != null ? anos : 5);
    }
}
