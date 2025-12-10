package com.importau.fornecedores.service;

import com.importau.fornecedores.domain.Fornecedor;
import com.importau.fornecedores.domain.Rating;
import com.importau.fornecedores.repository.FornecedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para FornecedorService
 * Implementa práticas de TDD
 */
@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

    private Fornecedor fornecedorTeste;

    @BeforeEach
    void setUp() {
        fornecedorTeste = new Fornecedor("FORN001", "TechSupply Brasil Ltda", "Brasil", "Tecnologia");
        fornecedorTeste.setRating(Rating.A);
        fornecedorTeste.setCnpj("12.345.678/0001-90");
        fornecedorTeste.setContatoEmail("contato@techsupply.com.br");
        fornecedorTeste.setTempoParceriaAnos(5);
    }

    @Test
    void deveCriarFornecedorComSucesso() {
        // Given
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorTeste);

        // When
        Fornecedor resultado = fornecedorService.create(fornecedorTeste);

        // Then
        assertNotNull(resultado);
        assertEquals("FORN001", resultado.getFornecedorId());
        assertTrue(resultado.isAtivo());
        verify(fornecedorRepository, times(1)).save(fornecedorTeste);
    }

    @Test
    void deveBuscarFornecedorPorId() {
        // Given
        when(fornecedorRepository.findById("FORN001")).thenReturn(Optional.of(fornecedorTeste));

        // When
        Optional<Fornecedor> resultado = fornecedorService.findById("FORN001");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("FORN001", resultado.get().getFornecedorId());
        verify(fornecedorRepository, times(1)).findById("FORN001");
    }

    @Test
    void deveAtivarFornecedorComSucesso() {
        // Given
        fornecedorTeste.setAtivo(false);
        when(fornecedorRepository.findById("FORN001")).thenReturn(Optional.of(fornecedorTeste));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorTeste);

        // When
        Fornecedor resultado = fornecedorService.ativar("FORN001");

        // Then
        assertTrue(resultado.isAtivo());
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void deveDesativarFornecedorComSucesso() {
        // Given
        when(fornecedorRepository.findById("FORN001")).thenReturn(Optional.of(fornecedorTeste));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorTeste);

        // When
        Fornecedor resultado = fornecedorService.desativar("FORN001");

        // Then
        assertFalse(resultado.isAtivo());
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void deveRealizarSoftDeleteComSucesso() {
        // Given
        when(fornecedorRepository.findById("FORN001")).thenReturn(Optional.of(fornecedorTeste));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorTeste);

        // When
        fornecedorService.delete("FORN001");

        // Then
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
        verify(fornecedorRepository, never()).deleteById(any());
    }

    @Test
    void deveLancarExcecaoAoBuscarFornecedorInexistente() {
        // Given
        when(fornecedorRepository.findById("FORN999")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> fornecedorService.ativar("FORN999"));
        
        assertEquals("Fornecedor não encontrado: FORN999", exception.getMessage());
    }

    @Test
    void deveAtualizarFornecedorExistente() {
        // Given
        Fornecedor fornecedorAtualizado = new Fornecedor();
        fornecedorAtualizado.setRazaoSocial("TechSupply Brasil Atualizada");
        fornecedorAtualizado.setRating(Rating.A_PLUS);
        
        when(fornecedorRepository.findById("FORN001")).thenReturn(Optional.of(fornecedorTeste));
        when(fornecedorRepository.save(any(Fornecedor.class))).thenReturn(fornecedorTeste);

        // When
        Fornecedor resultado = fornecedorService.update("FORN001", fornecedorAtualizado);

        // Then
        assertNotNull(resultado);
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void deveIdentificarFornecedorInternacional() {
        // Given
        fornecedorTeste.setPaisOrigem("Estados Unidos");

        // When
        boolean isInternacional = fornecedorTeste.isFornecedorInternacional();

        // Then
        assertTrue(isInternacional);
    }

    @Test
    void deveIdentificarParceriaLongaPrazo() {
        // Given
        fornecedorTeste.setTempoParceriaAnos(7);

        // When
        boolean isParceriaLonga = fornecedorTeste.isParceriaLongaPrazo();

        // Then
        assertTrue(isParceriaLonga);
    }
}
