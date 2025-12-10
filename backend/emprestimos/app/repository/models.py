"""
Modelos de banco de dados usando SQLAlchemy
"""
from datetime import datetime
from sqlalchemy import Column, String, Numeric, Integer, Date, DateTime, Enum as SQLEnum
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.sql import func
import enum

Base = declarative_base()


class StatusEmprestimoEnum(enum.Enum):
    """Enum para status do empréstimo"""
    ATIVO = "ATIVO"
    QUITADO = "QUITADO"
    INADIMPLENTE = "INADIMPLENTE"
    RENEGOCIADO = "RENEGOCIADO"


class EmprestimoModel(Base):
    """Modelo de banco de dados para Emprestimo"""
    __tablename__ = "emprestimos"
    
    emprestimo_id = Column(String(50), primary_key=True)
    data_contratacao = Column(Date, nullable=False)
    instituicao_financeira = Column(String(200), nullable=False)
    valor_principal_brl = Column(Numeric(15, 2), nullable=False)
    taxa_juros_anual = Column(Numeric(5, 2), nullable=False)
    prazo_meses = Column(Integer, nullable=False)
    valor_parcela_mensal = Column(Numeric(15, 2), nullable=False)
    saldo_devedor = Column(Numeric(15, 2), nullable=False)
    status = Column(SQLEnum(StatusEmprestimoEnum), nullable=False, default=StatusEmprestimoEnum.ATIVO)
    finalidade = Column(String(200), nullable=False)
    data_vencimento_proxima = Column(Date)
    num_parcelas_pagas = Column(Integer, nullable=False, default=0)
    usuario_responsavel = Column(String(100), nullable=False)
    created_at = Column(DateTime, nullable=False, server_default=func.now())
    updated_at = Column(DateTime, nullable=False, server_default=func.now(), onupdate=func.now())
