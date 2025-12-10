package com.importau.fornecedores.controller;

import com.importau.fornecedores.domain.Fornecedor;
import com.importau.fornecedores.domain.Rating;
import com.importau.fornecedores.service.FornecedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller REST para operações de Fornecedores
 * Implementa endpoints compatíveis com react-admin (ra-data-simple-rest)
 */
@RestController
@RequestMapping("/api/v1/fornecedores")
@CrossOrigin(origins = "*")
@Tag(name = "Fornecedores", description = "API para gerenciamento de fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    /**
     * Lista fornecedores com paginação e filtros (compatível com react-admin)
     */
    @GetMapping
    @Operation(summary = "Lista fornecedores", description = "Retorna lista paginada de fornecedores")
    public ResponseEntity<List<Fornecedor>> getAllFornecedores(
            @Parameter(description = "Página (base 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "razaoSocial") String sort,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "asc") String order,
            @Parameter(description = "Filtro por categoria") @RequestParam(required = false) String categoria,
            @Parameter(description = "Filtro por país") @RequestParam(required = false) String pais,
            @Parameter(description = "Filtro por rating") @RequestParam(required = false) Rating rating) {

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Fornecedor> fornecedores;

        // Aplicar filtros específicos se fornecidos
        if (categoria != null) {
            fornecedores = fornecedorService.findByCategoria(categoria, pageable);
        } else {
            fornecedores = fornecedorService.findAll(pageable);
        }

        // Adicionar headers para react-admin
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(fornecedores.getTotalElements()))
                .header("Access-Control-Expose-Headers", "X-Total-Count")
                .body(fornecedores.getContent());
    }

    /**
     * Busca fornecedor por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca fornecedor por ID")
    public ResponseEntity<Fornecedor> getFornecedorById(@PathVariable String id) {
        return fornecedorService.findById(id)
                .map(fornecedor -> ResponseEntity.ok(fornecedor))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria novo fornecedor
     */
    @PostMapping
    @Operation(summary = "Cria novo fornecedor")
    public ResponseEntity<Fornecedor> createFornecedor(@Valid @RequestBody Fornecedor fornecedor) {
        Fornecedor novoFornecedor = fornecedorService.create(fornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFornecedor);
    }

    /**
     * Atualiza fornecedor existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza fornecedor")
    public ResponseEntity<Fornecedor> updateFornecedor(@PathVariable String id, 
                                                      @Valid @RequestBody Fornecedor fornecedor) {
        try {
            Fornecedor fornecedorAtualizado = fornecedorService.update(id, fornecedor);
            return ResponseEntity.ok(fornecedorAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove fornecedor (soft delete)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove fornecedor")
    public ResponseEntity<Void> deleteFornecedor(@PathVariable String id) {
        try {
            fornecedorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca fornecedores por país
     */
    @GetMapping("/pais/{pais}")
    @Operation(summary = "Busca fornecedores por país")
    public ResponseEntity<List<Fornecedor>> getFornecedoresByPais(@PathVariable String pais) {
        List<Fornecedor> fornecedores = fornecedorService.findByPais(pais);
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * Busca fornecedores por rating
     */
    @GetMapping("/rating/{rating}")
    @Operation(summary = "Busca fornecedores por rating")
    public ResponseEntity<List<Fornecedor>> getFornecedoresByRating(@PathVariable Rating rating) {
        List<Fornecedor> fornecedores = fornecedorService.findByRating(rating);
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * Busca fornecedores ativos
     */
    @GetMapping("/ativos")
    @Operation(summary = "Busca fornecedores ativos")
    public ResponseEntity<List<Fornecedor>> getFornecedoresAtivos() {
        List<Fornecedor> fornecedores = fornecedorService.findAtivos();
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * Busca fornecedores por razão social
     */
    @GetMapping("/buscar")
    @Operation(summary = "Busca fornecedores por razão social")
    public ResponseEntity<List<Fornecedor>> buscarFornecedores(
            @RequestParam String razaoSocial) {
        List<Fornecedor> fornecedores = fornecedorService.findByRazaoSocial(razaoSocial);
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * Ativa fornecedor
     */
    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativa fornecedor")
    public ResponseEntity<Fornecedor> ativarFornecedor(@PathVariable String id) {
        try {
            Fornecedor fornecedor = fornecedorService.ativar(id);
            return ResponseEntity.ok(fornecedor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Desativa fornecedor
     */
    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativa fornecedor")
    public ResponseEntity<Fornecedor> desativarFornecedor(@PathVariable String id) {
        try {
            Fornecedor fornecedor = fornecedorService.desativar(id);
            return ResponseEntity.ok(fornecedor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca fornecedores com parceria de longo prazo
     */
    @GetMapping("/parceria-longa")
    @Operation(summary = "Busca fornecedores com parceria de longo prazo")
    public ResponseEntity<List<Fornecedor>> getFornecedoresParceriaLonga(
            @RequestParam(defaultValue = "5") Integer anos) {
        List<Fornecedor> fornecedores = fornecedorService.findParceriaLongaPrazo(anos);
        return ResponseEntity.ok(fornecedores);
    }
}
