"""
Testes unitários para CambioService
"""
import pytest
from datetime import date, time
from decimal import Decimal
from unittest.mock import Mock
from app.domain.cambio import Cambio, TipoCambio
from app.service.cambio_service import CambioService


@pytest.fixture
def mock_repository():
    return Mock()


@pytest.fixture
def service(mock_repository):
    return CambioService(mock_repository)


@pytest.fixture
def cambio_valido():
    return Cambio(
        cambio_id=1,
        data_cotacao=date(2024, 10, 15),
        moeda="USD",
        taxa_compra=Decimal("5.2350"),
        taxa_venda=Decimal("5.2580"),
        taxa_ptax=Decimal("5.2465"),
        variacao_dia_percentual=Decimal("0.15"),
        fonte="SISBACEN",
        tipo_cambio=TipoCambio.COMERCIAL,
        hora_atualizacao=time(16, 30, 0)
    )


def test_listar_cotacoes(service, mock_repository, cambio_valido):
    """Testa listagem de cotações"""
    mock_repository.find_all.return_value = [cambio_valido]
    
    resultado = service.listar_cotacoes()
    
    assert len(resultado) == 1
    assert resultado[0].moeda == "USD"
    mock_repository.find_all.assert_called_once()


def test_buscar_por_moeda(service, mock_repository, cambio_valido):
    """Testa busca por moeda"""
    mock_repository.find_by_moeda.return_value = [cambio_valido]
    
    resultado = service.buscar_por_moeda("USD")
    
    assert len(resultado) == 1
    assert resultado[0].moeda == "USD"


def test_criar_cotacao_valida(service, mock_repository, cambio_valido):
    """Testa criação de cotação válida"""
    mock_repository.save.return_value = cambio_valido
    
    resultado = service.criar_cotacao(cambio_valido)
    
    assert resultado.moeda == "USD"
    mock_repository.save.assert_called_once()


def test_criar_cotacao_taxa_invalida(service, cambio_valido):
    """Testa validação de taxa venda menor que compra"""
    cambio_valido.taxa_venda = Decimal("5.0000")
    
    with pytest.raises(ValueError, match="Taxa de venda deve ser maior"):
        service.criar_cotacao(cambio_valido)


def test_spread(cambio_valido):
    """Testa cálculo de spread"""
    assert cambio_valido.spread == Decimal("0.0230")


def test_spread_percentual(cambio_valido):
    """Testa cálculo de spread percentual"""
    assert cambio_valido.spread_percentual == 0.4394


def test_variacao_positiva(cambio_valido):
    """Testa identificação de variação positiva"""
    assert cambio_valido.variacao_positiva == True
    
    cambio_valido.variacao_dia_percentual = Decimal("-0.15")
    assert cambio_valido.variacao_positiva == False
