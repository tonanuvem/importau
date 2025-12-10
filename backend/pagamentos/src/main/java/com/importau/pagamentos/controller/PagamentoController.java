package com.importau.pagamentos.controller;

import com.importau.pagamentos.domain.Pagamento;
import com.importau.pagamentos.domain.StatusPagamento;
import com.importau.pagamentos.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para operações de Pagamentos
 * Implementa endpoints compatíveis com react-admin (ra-data-simple-rest)
 */
@RestController
@RequestMapping("/api/v1/pagamentos")
@CrossOrigin(origins = "*")
@Tag(name = "Pagamentos", description = "API para gerenciamento de pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Autowired
    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    /**
     * Lista pagamentos com paginação e filtros (compatível com react-admin)
     */
    @GetMapping
    @Operation(summary = "Lista pagamentos", description = "Retorna lista paginada de pagamentos")
    public ResponseEntity<List<Pagamento>> getAllPagamentos(
            @Parameter(description = "Página (base 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "desc") String order,
            @Parameter(description = "Filtro por pedido") @RequestParam(required = false) String pedidoId,
            @Parameter(description = "Filtro por fornecedor") @RequestParam(required = false) String fornecedorId,
            @Parameter(description = "Filtro por status") @RequestParam(required = false) StatusPagamento status) {

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Pagamento> pagamentos;

        // Aplicar filtros específicos se fornecidos
        if (fornecedorId != null) {
            pagamentos = pagamentoService.findByFornecedorId(fornecedorId, pageable);
        } else {
            pagamentos = pagamentoService.findAll(pageable);
        }

        // Adicionar headers para react-admin
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(pagamentos.getTotalElements()))
                .header("Access-Control-Expose-Headers", "X-Total-Count")
                .body(pagamentos.getContent());
    }

    /**
     * Busca pagamento por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca pagamento por ID")
    public ResponseEntity<Pagamento> getPagamentoById(@PathVariable String id) {
        return pagamentoService.findById(id)
                .map(pagamento -> ResponseEntity.ok(pagamento))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria novo pagamento
     */
    @PostMapping
    @Operation(summary = "Cria novo pagamento")
    public ResponseEntity<Pagamento> createPagamento(@Valid @RequestBody Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.create(pagamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    /**
     * Atualiza pagamento existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza pagamento")
    public ResponseEntity<Pagamento> updatePagamento(@PathVariable String id, 
                                                    @Valid @RequestBody Pagamento pagamento) {
        try {
            Pagamento pagamentoAtualizado = pagamentoService.update(id, pagamento);
            return ResponseEntity.ok(pagamentoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Remove pagamento
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove pagamento")
    public ResponseEntity<Void> deletePagamento(@PathVariable String id) {
        try {
            pagamentoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca pagamentos por pedido
     */
    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Busca pagamentos por pedido")
    public ResponseEntity<List<Pagamento>> getPagamentosByPedido(@PathVariable String pedidoId) {
        List<Pagamento> pagamentos = pagamentoService.findByPedidoId(pedidoId);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Busca pagamentos por status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Busca pagamentos por status")
    public ResponseEntity<List<Pagamento>> getPagamentosByStatus(@PathVariable StatusPagamento status) {
        List<Pagamento> pagamentos = pagamentoService.findByStatus(status);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Busca pagamentos por período
     */
    @GetMapping("/periodo")
    @Operation(summary = "Busca pagamentos por período")
    public ResponseEntity<List<Pagamento>> getPagamentosByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<Pagamento> pagamentos = pagamentoService.findByPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(pagamentos);
    }

    /**
     * Confirma pagamento
     */
    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirma pagamento")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable String id,
                                                       @RequestBody Map<String, String> request) {
        try {
            String usuarioAprovacao = request.get("usuarioAprovacao");
            Pagamento pagamento = pagamentoService.confirmarPagamento(id, usuarioAprovacao);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancela pagamento
     */
    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancela pagamento")
    public ResponseEntity<Pagamento> cancelarPagamento(@PathVariable String id,
                                                      @RequestBody Map<String, String> request) {
        try {
            String usuarioAprovacao = request.get("usuarioAprovacao");
            Pagamento pagamento = pagamentoService.cancelarPagamento(id, usuarioAprovacao);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Busca pagamentos com vencimento próximo
     */
    @GetMapping("/vencimento-proximo")
    @Operation(summary = "Busca pagamentos com vencimento próximo")
    public ResponseEntity<List<Pagamento>> getPagamentosVencimentoProximo(
            @RequestParam(defaultValue = "7") int dias) {
        List<Pagamento> pagamentos = pagamentoService.findPagamentosVencimentoProximo(dias);
        return ResponseEntity.ok(pagamentos);
    }
}
