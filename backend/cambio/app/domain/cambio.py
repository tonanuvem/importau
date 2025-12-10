"""
Entidade de domínio Cambio
Representa cotações de moedas estrangeiras
"""
from datetime import datetime, date, time
from decimal import Decimal
from enum import Enum
from typing import Optional
from pydantic import BaseModel, Field


class TipoCambio(str, Enum):
    """Tipos de câmbio"""
    COMERCIAL = "COMERCIAL"
    TURISMO = "TURISMO"
    PARALELO = "PARALELO"


class Cambio(BaseModel):
    """Modelo de domínio para Cambio"""
    cambio_id: Optional[int] = None
    data_cotacao: date
    moeda: str = Field(min_length=3, max_length=3)
    taxa_compra: Decimal = Field(gt=0)
    taxa_venda: Decimal = Field(gt=0)
    taxa_ptax: Decimal = Field(gt=0)
    variacao_dia_percentual: Decimal
    fonte: str = Field(min_length=1)
    tipo_cambio: TipoCambio
    hora_atualizacao: time
    created_at: Optional[datetime] = None
    updated_at: Optional[datetime] = None
    
    # Campos calculados
    @property
    def spread(self) -> Decimal:
        """Calcula spread entre compra e venda"""
        return self.taxa_venda - self.taxa_compra
    
    @property
    def spread_percentual(self) -> float:
        """Calcula spread percentual"""
        if self.taxa_compra == 0:
            return 0.0
        return round(float((self.spread / self.taxa_compra) * 100), 4)
    
    @property
    def variacao_positiva(self) -> bool:
        """Verifica se variação do dia é positiva"""
        return self.variacao_dia_percentual > 0

    class Config:
        from_attributes = True
        json_encoders = {
            Decimal: lambda v: float(v),
            date: lambda v: v.isoformat(),
            time: lambda v: v.isoformat(),
            datetime: lambda v: v.isoformat()
        }
