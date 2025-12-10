"""
Repositório para operações de persistência de Cambio
"""
from typing import List, Optional
from datetime import date
from sqlalchemy.orm import Session
from sqlalchemy import desc
from .models import CambioModel, TipoCambioEnum
from ..domain.cambio import Cambio, TipoCambio


class CambioRepository:
    """Repositório para gerenciar cotações de câmbio"""
    
    def __init__(self, db: Session):
        self.db = db
    
    def _to_domain(self, model: CambioModel) -> Cambio:
        """Converte modelo de BD para entidade de domínio"""
        return Cambio(
            cambio_id=model.cambio_id,
            data_cotacao=model.data_cotacao,
            moeda=model.moeda,
            taxa_compra=model.taxa_compra,
            taxa_venda=model.taxa_venda,
            taxa_ptax=model.taxa_ptax,
            variacao_dia_percentual=model.variacao_dia_percentual,
            fonte=model.fonte,
            tipo_cambio=TipoCambio(model.tipo_cambio.value),
            hora_atualizacao=model.hora_atualizacao,
            created_at=model.created_at,
            updated_at=model.updated_at
        )
    
    def find_all(self, skip: int = 0, limit: int = 100) -> List[Cambio]:
        """Busca todas as cotações com paginação"""
        models = self.db.query(CambioModel).order_by(
            desc(CambioModel.data_cotacao)
        ).offset(skip).limit(limit).all()
        return [self._to_domain(m) for m in models]
    
    def find_by_id(self, cambio_id: int) -> Optional[Cambio]:
        """Busca cotação por ID"""
        model = self.db.query(CambioModel).filter(
            CambioModel.cambio_id == cambio_id
        ).first()
        return self._to_domain(model) if model else None
    
    def find_by_moeda(self, moeda: str) -> List[Cambio]:
        """Busca cotações por moeda"""
        models = self.db.query(CambioModel).filter(
            CambioModel.moeda == moeda.upper()
        ).order_by(desc(CambioModel.data_cotacao)).all()
        return [self._to_domain(m) for m in models]
    
    def find_by_data(self, data: date) -> List[Cambio]:
        """Busca cotações por data"""
        models = self.db.query(CambioModel).filter(
            CambioModel.data_cotacao == data
        ).all()
        return [self._to_domain(m) for m in models]
    
    def find_ultima_cotacao_moeda(self, moeda: str) -> Optional[Cambio]:
        """Busca última cotação de uma moeda"""
        model = self.db.query(CambioModel).filter(
            CambioModel.moeda == moeda.upper()
        ).order_by(desc(CambioModel.data_cotacao)).first()
        return self._to_domain(model) if model else None
    
    def save(self, cambio: Cambio) -> Cambio:
        """Salva ou atualiza uma cotação"""
        if cambio.cambio_id:
            model = self.db.query(CambioModel).filter(
                CambioModel.cambio_id == cambio.cambio_id
            ).first()
            if model:
                for key, value in cambio.model_dump(exclude={'created_at', 'updated_at'}).items():
                    if key == 'tipo_cambio':
                        setattr(model, key, TipoCambioEnum[value])
                    else:
                        setattr(model, key, value)
        else:
            model = CambioModel(
                **{k: v if k != 'tipo_cambio' else TipoCambioEnum[v] 
                   for k, v in cambio.model_dump(exclude={'cambio_id', 'created_at', 'updated_at'}).items()}
            )
            self.db.add(model)
        
        self.db.commit()
        self.db.refresh(model)
        return self._to_domain(model)
    
    def delete(self, cambio_id: int) -> bool:
        """Remove uma cotação"""
        model = self.db.query(CambioModel).filter(
            CambioModel.cambio_id == cambio_id
        ).first()
        
        if model:
            self.db.delete(model)
            self.db.commit()
            return True
        return False
    
    def count(self) -> int:
        """Conta total de cotações"""
        return self.db.query(CambioModel).count()
