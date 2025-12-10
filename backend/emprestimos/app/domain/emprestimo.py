"""
Entidade de domínio Emprestimo
Representa um empréstimo financeiro contraído pela empresa
"""
from datetime import datetime, date
from decimal import Decimal
from enum import Enum
from typing import Optional
from pydantic import BaseModel, Field


class StatusEmprestimo(str, Enum):
    """Status possíveis de um empréstimo"""
    ATIVO = "ATIVO"
    QUITADO = "QUITADO"
    INADIMPLENTE = "INADIMPLENTE"
    RENEGOCIADO = "RENEGOCIADO"


class Emprestimo(BaseModel):
    """Modelo de domínio para Emprestimo"""
    emprestimo_id: Optional[str] = None
    data_contratacao: date
    instituicao_financeira: str = Field(min_length=1)
    valor_principal_brl: Decimal = Field(gt=0)
    taxa_juros_anual: Decimal = Field(ge=0)
    prazo_meses: int = Field(gt=0)
    valor_parcela_mensal: Decimal = Field(gt=0)
    saldo_devedor: Decimal = Field(ge=0)
    status: StatusEmprestimo
    finalidade: str = Field(min_length=1)
    data_vencimento_proxima: Optional[date] = None
    num_parcelas_pagas: int = Field(ge=0, default=0)
    usuario_responsavel: str = Field(min_length=1)
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    
    # Campos calculados
    @property
    def percentual_pago(self) -> float:
        """Calcula percentual pago do empréstimo"""
        if self.prazo_meses == 0:
            return 0.0
        return round((self.num_parcelas_pagas / self.prazo_meses) * 100, 2)
    
    @property
    def valor_total_emprestimo(self) -> Decimal:
        """Calcula valor total do empréstimo com juros"""
        return self.valor_parcela_mensal * self.prazo_meses
    
    @property
    def emprestimo_longo_prazo(self) -> bool:
        """Verifica se é empréstimo de longo prazo (> 36 meses)"""
        return self.prazo_meses > 36

    class Config:
        from_attributes = True
        json_encoders = {
            Decimal: lambda v: float(v),
            date: lambda v: v.isoformat(),
            datetime: lambda v: v.isoformat()
        }
