"""
Serviço de negócio para Emprestimos
Contém lógica de negócio e validações
"""
from typing import List, Optional
from datetime import datetime
from ..domain.emprestimo import Emprestimo, StatusEmprestimo
from ..repository.emprestimo_repository import EmprestimoRepository


class EmprestimoService:
    """Serviço para gerenciar regras de negócio de empréstimos"""
    
    def __init__(self, repository: EmprestimoRepository):
        self.repository = repository
    
    def listar_emprestimos(self, skip: int = 0, limit: int = 100) -> List[Emprestimo]:
        """Lista todos os empréstimos"""
        return self.repository.find_all(skip, limit)
    
    def buscar_por_id(self, emprestimo_id: str) -> Optional[Emprestimo]:
        """Busca empréstimo por ID"""
        return self.repository.find_by_id(emprestimo_id)
    
    def buscar_por_status(self, status: StatusEmprestimo) -> List[Emprestimo]:
        """Busca empréstimos por status"""
        return self.repository.find_by_status(status)
    
    def buscar_por_instituicao(self, instituicao: str) -> List[Emprestimo]:
        """Busca empréstimos por instituição financeira"""
        return self.repository.find_by_instituicao(instituicao)
    
    def criar_emprestimo(self, emprestimo: Emprestimo) -> Emprestimo:
        """Cria novo empréstimo com validações"""
        # Gera ID se não fornecido
        if not emprestimo.emprestimo_id:
            emprestimo.emprestimo_id = self._gerar_id()
        
        # Validações de negócio
        if emprestimo.saldo_devedor > emprestimo.valor_principal_brl:
            raise ValueError("Saldo devedor não pode ser maior que valor principal")
        
        if emprestimo.num_parcelas_pagas > emprestimo.prazo_meses:
            raise ValueError("Número de parcelas pagas não pode exceder prazo total")
        
        return self.repository.save(emprestimo)
    
    def atualizar_emprestimo(self, emprestimo_id: str, emprestimo: Emprestimo) -> Optional[Emprestimo]:
        """Atualiza empréstimo existente"""
        existente = self.repository.find_by_id(emprestimo_id)
        if not existente:
            return None
        
        emprestimo.emprestimo_id = emprestimo_id
        return self.repository.save(emprestimo)
    
    def deletar_emprestimo(self, emprestimo_id: str) -> bool:
        """Remove empréstimo"""
        return self.repository.delete(emprestimo_id)
    
    def contar_emprestimos(self) -> int:
        """Conta total de empréstimos"""
        return self.repository.count()
    
    def _gerar_id(self) -> str:
        """Gera ID único para empréstimo"""
        import uuid
        return f"EMP{str(uuid.uuid4())[:8].upper()}"
