"""
Modelos de banco de dados usando SQLAlchemy
"""
from datetime import datetime
from sqlalchemy import Column, Integer, String, Numeric, Date, Time, DateTime, Enum as SQLEnum
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.sql import func
import enum

Base = declarative_base()


class TipoCambioEnum(enum.Enum):
    """Enum para tipo de câmbio"""
    COMERCIAL = "COMERCIAL"
    TURISMO = "TURISMO"
    PARALELO = "PARALELO"


class CambioModel(Base):
    """Modelo de banco de dados para Cambio"""
    __tablename__ = "cambio"
    
    cambio_id = Column(Integer, primary_key=True, autoincrement=True)
    data_cotacao = Column(Date, nullable=False, index=True)
    moeda = Column(String(3), nullable=False, index=True)
    taxa_compra = Column(Numeric(10, 4), nullable=False)
    taxa_venda = Column(Numeric(10, 4), nullable=False)
    taxa_ptax = Column(Numeric(10, 4), nullable=False)
    variacao_dia_percentual = Column(Numeric(6, 2), nullable=False)
    fonte = Column(String(50), nullable=False)
    tipo_cambio = Column(SQLEnum(TipoCambioEnum), nullable=False, default=TipoCambioEnum.COMERCIAL)
    hora_atualizacao = Column(Time, nullable=False)
    created_at = Column(DateTime, nullable=False, server_default=func.now())
    updated_at = Column(DateTime, nullable=False, server_default=func.now(), onupdate=func.now())
