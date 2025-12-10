"""
Repositório para operações de persistência de Emprestimos
"""
from typing import List, Optional
from sqlalchemy.orm import Session
from .models import EmprestimoModel, StatusEmprestimoEnum
from ..domain.emprestimo import Emprestimo, StatusEmprestimo


class EmprestimoRepository:
    """Repositório para gerenciar empréstimos no banco de dados"""
    
    def __init__(self, db: Session):
        self.db = db
    
    def _to_domain(self, model: EmprestimoModel) -> Emprestimo:
        """Converte modelo de BD para entidade de domínio"""
        return Emprestimo(
            emprestimo_id=model.emprestimo_id,
            data_contratacao=model.data_contratacao,
            instituicao_financeira=model.instituicao_financeira,
            valor_principal_brl=model.valor_principal_brl,
            taxa_juros_anual=model.taxa_juros_anual,
            prazo_meses=model.prazo_meses,
            valor_parcela_mensal=model.valor_parcela_mensal,
            saldo_devedor=model.saldo_devedor,
            status=StatusEmprestimo(model.status.value),
            finalidade=model.finalidade,
            data_vencimento_proxima=model.data_vencimento_proxima,
            num_parcelas_pagas=model.num_parcelas_pagas,
            usuario_responsavel=model.usuario_responsavel,
            created_at=model.created_at,
            updated_at=model.updated_at
        )
    
    def find_all(self, skip: int = 0, limit: int = 100) -> List[Emprestimo]:
        """Busca todos os empréstimos com paginação"""
        models = self.db.query(EmprestimoModel).offset(skip).limit(limit).all()
        return [self._to_domain(m) for m in models]
    
    def find_by_id(self, emprestimo_id: str) -> Optional[Emprestimo]:
        """Busca empréstimo por ID"""
        model = self.db.query(EmprestimoModel).filter(
            EmprestimoModel.emprestimo_id == emprestimo_id
        ).first()
        return self._to_domain(model) if model else None
    
    def find_by_status(self, status: StatusEmprestimo) -> List[Emprestimo]:
        """Busca empréstimos por status"""
        status_enum = StatusEmprestimoEnum[status.value]
        models = self.db.query(EmprestimoModel).filter(
            EmprestimoModel.status == status_enum
        ).all()
        return [self._to_domain(m) for m in models]
    
    def find_by_instituicao(self, instituicao: str) -> List[Emprestimo]:
        """Busca empréstimos por instituição financeira"""
        models = self.db.query(EmprestimoModel).filter(
            EmprestimoModel.instituicao_financeira.ilike(f"%{instituicao}%")
        ).all()
        return [self._to_domain(m) for m in models]
    
    def save(self, emprestimo: Emprestimo) -> Emprestimo:
        """Salva ou atualiza um empréstimo"""
        model = self.db.query(EmprestimoModel).filter(
            EmprestimoModel.emprestimo_id == emprestimo.emprestimo_id
        ).first()
        
        if model:
            # Atualiza existente
            for key, value in emprestimo.model_dump(exclude={'created_at', 'updated_at'}).items():
                if key == 'status':
                    setattr(model, key, StatusEmprestimoEnum[value])
                else:
                    setattr(model, key, value)
        else:
            # Cria novo
            model = EmprestimoModel(
                **{k: v if k != 'status' else StatusEmprestimoEnum[v] 
                   for k, v in emprestimo.model_dump(exclude={'created_at', 'updated_at'}).items()}
            )
            self.db.add(model)
        
        self.db.commit()
        self.db.refresh(model)
        return self._to_domain(model)
    
    def delete(self, emprestimo_id: str) -> bool:
        """Remove um empréstimo"""
        model = self.db.query(EmprestimoModel).filter(
            EmprestimoModel.emprestimo_id == emprestimo_id
        ).first()
        
        if model:
            self.db.delete(model)
            self.db.commit()
            return True
        return False
    
    def count(self) -> int:
        """Conta total de empréstimos"""
        return self.db.query(EmprestimoModel).count()
