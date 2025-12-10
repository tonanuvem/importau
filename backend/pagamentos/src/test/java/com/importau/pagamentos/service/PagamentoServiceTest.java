package com.importau.pagamentos.service;

import com.importau.pagamentos.domain.Pagamento;
import com.importau.pagamentos.domain.StatusPagamento;
import com.importau.pagamentos.domain.MetodoPagamento;
import com.importau.pagamentos.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para PagamentoService
 * Implementa práticas de TDD
 */
@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamentoTeste;

    @BeforeEach
    void setUp() {
        pagamentoTeste = new Pagamento("PAG001", "PED001", "FORN001");
        pagamentoTeste.setValorPagoBrl(new BigDecimal("1000.00"));
        pagamentoTeste.setMetodoPagamento(MetodoPagamento.PIX);
        pagamentoTeste.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamentoTeste.setDataVencimento(LocalDate.now().plusDays(7));
    }

    @Test
    void deveCriarPagamentoComSucesso() {
        // Given
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoTeste);

        // When
        Pagamento resultado = pagamentoService.create(pagamentoTeste);

        // Then
        assertNotNull(resultado);
        assertEquals("PAG001", resultado.getPagamentoId());
        assertEquals(StatusPagamento.PENDENTE, resultado.getStatusPagamento());
        verify(pagamentoRepository, times(1)).save(pagamentoTeste);
    }

    @Test
    void deveBuscarPagamentoPorId() {
        // Given
        when(pagamentoRepository.findById("PAG001")).thenReturn(Optional.of(pagamentoTeste));

        // When
        Optional<Pagamento> resultado = pagamentoService.findById("PAG001");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("PAG001", resultado.get().getPagamentoId());
        verify(pagamentoRepository, times(1)).findById("PAG001");
    }

    @Test
    void deveConfirmarPagamentoComSucesso() {
        // Given
        when(pagamentoRepository.findById("PAG001")).thenReturn(Optional.of(pagamentoTeste));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoTeste);

        // When
        Pagamento resultado = pagamentoService.confirmarPagamento("PAG001", "usuario.teste");

        // Then
        assertEquals(StatusPagamento.PAGO, resultado.getStatusPagamento());
        assertEquals("usuario.teste", resultado.getUsuarioAprovacao());
        assertNotNull(resultado.getDataPagamento());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void deveCancelarPagamentoPendente() {
        // Given
        when(pagamentoRepository.findById("PAG001")).thenReturn(Optional.of(pagamentoTeste));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoTeste);

        // When
        Pagamento resultado = pagamentoService.cancelarPagamento("PAG001", "usuario.teste");

        // Then
        assertEquals(StatusPagamento.CANCELADO, resultado.getStatusPagamento());
        assertEquals("usuario.teste", resultado.getUsuarioAprovacao());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void naoDeveCancelarPagamentoJaPago() {
        // Given
        pagamentoTeste.setStatusPagamento(StatusPagamento.PAGO);
        when(pagamentoRepository.findById("PAG001")).thenReturn(Optional.of(pagamentoTeste));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> pagamentoService.cancelarPagamento("PAG001", "usuario.teste"));
        
        assertEquals("Não é possível cancelar pagamento já realizado", exception.getMessage());
        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarPagamentoInexistente() {
        // Given
        when(pagamentoRepository.findById("PAG999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> pagamentoService.confirmarPagamento("PAG999", "usuario.teste"));
        
        assertEquals("Pagamento não encontrado: PAG999", exception.getMessage());
    }

    @Test
    void deveAtualizarPagamentoExistente() {
        // Given
        Pagamento pagamentoAtualizado = new Pagamento();
        pagamentoAtualizado.setValorPagoBrl(new BigDecimal("2000.00"));
        pagamentoAtualizado.setMetodoPagamento(MetodoPagamento.BOLETO);
        
        when(pagamentoRepository.findById("PAG001")).thenReturn(Optional.of(pagamentoTeste));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamentoTeste);

        // When
        Pagamento resultado = pagamentoService.update("PAG001", pagamentoAtualizado);

        // Then
        assertNotNull(resultado);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }
}
