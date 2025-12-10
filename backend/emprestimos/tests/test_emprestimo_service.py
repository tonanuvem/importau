"""
Testes unitários para EmprestimoService
"""
import pytest
from datetime import date
from decimal import Decimal
from unittest.mock import Mock
from app.domain.emprestimo import Emprestimo, StatusEmprestimo
from app.service.emprestimo_service import EmprestimoService


@pytest.fixture
def mock_repository():
    """Mock do repositório"""
    return Mock()


@pytest.fixture
def service(mock_repository):
    """Instância do serviço com repositório mockado"""
    return EmprestimoService(mock_repository)


@pytest.fixture
def emprestimo_valido():
    """Empréstimo válido para testes"""
    return Emprestimo(
        emprestimo_id="EMP001",
        data_contratacao=date(2024, 1, 15),
        instituicao_financeira="Banco Teste",
        valor_principal_brl=Decimal("100000.00"),
        taxa_juros_anual=Decimal("12.5"),
        prazo_meses=36,
        valor_parcela_mensal=Decimal("3500.00"),
        saldo_devedor=Decimal("80000.00"),
        status=StatusEmprestimo.ATIVO,
        finalidade="Capital de Giro",
        num_parcelas_pagas=10,
        usuario_responsavel="teste.usuario"
    )


def test_listar_emprestimos(service, mock_repository, emprestimo_valido):
    """Testa listagem de empréstimos"""
    mock_repository.find_all.return_value = [emprestimo_valido]
    
    resultado = service.listar_emprestimos()
    
    assert len(resultado) == 1
    assert resultado[0].emprestimo_id == "EMP001"
    mock_repository.find_all.assert_called_once()


def test_buscar_por_id(service, mock_repository, emprestimo_valido):
    """Testa busca por ID"""
    mock_repository.find_by_id.return_value = emprestimo_valido
    
    resultado = service.buscar_por_id("EMP001")
    
    assert resultado is not None
    assert resultado.emprestimo_id == "EMP001"
    mock_repository.find_by_id.assert_called_once_with("EMP001")


def test_criar_emprestimo_valido(service, mock_repository, emprestimo_valido):
    """Testa criação de empréstimo válido"""
    mock_repository.save.return_value = emprestimo_valido
    
    resultado = service.criar_emprestimo(emprestimo_valido)
    
    assert resultado.emprestimo_id == "EMP001"
    mock_repository.save.assert_called_once()


def test_criar_emprestimo_saldo_invalido(service, emprestimo_valido):
    """Testa validação de saldo devedor maior que principal"""
    emprestimo_valido.saldo_devedor = Decimal("150000.00")
    
    with pytest.raises(ValueError, match="Saldo devedor não pode ser maior"):
        service.criar_emprestimo(emprestimo_valido)


def test_criar_emprestimo_parcelas_invalidas(service, emprestimo_valido):
    """Testa validação de parcelas pagas maior que prazo"""
    emprestimo_valido.num_parcelas_pagas = 50
    
    with pytest.raises(ValueError, match="parcelas pagas não pode exceder"):
        service.criar_emprestimo(emprestimo_valido)


def test_percentual_pago(emprestimo_valido):
    """Testa cálculo de percentual pago"""
    assert emprestimo_valido.percentual_pago == 27.78


def test_valor_total_emprestimo(emprestimo_valido):
    """Testa cálculo de valor total"""
    assert emprestimo_valido.valor_total_emprestimo == Decimal("126000.00")


def test_emprestimo_longo_prazo(emprestimo_valido):
    """Testa identificação de empréstimo de longo prazo"""
    assert emprestimo_valido.emprestimo_longo_prazo == False
    
    emprestimo_valido.prazo_meses = 48
    assert emprestimo_valido.emprestimo_longo_prazo == True
