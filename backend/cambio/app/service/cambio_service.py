"""
Serviço de negócio para Cambio
"""
from typing import List, Optional
from datetime import date
from ..domain.cambio import Cambio
from ..repository.cambio_repository import CambioRepository


class CambioService:
    """Serviço para gerenciar regras de negócio de câmbio"""
    
    def __init__(self, repository: CambioRepository):
        self.repository = repository
    
    def listar_cotacoes(self, skip: int = 0, limit: int = 100) -> List[Cambio]:
        """Lista todas as cotações"""
        return self.repository.find_all(skip, limit)
    
    def buscar_por_id(self, cambio_id: int) -> Optional[Cambio]:
        """Busca cotação por ID"""
        return self.repository.find_by_id(cambio_id)
    
    def buscar_por_moeda(self, moeda: str) -> List[Cambio]:
        """Busca cotações por moeda"""
        return self.repository.find_by_moeda(moeda)
    
    def buscar_por_data(self, data: date) -> List[Cambio]:
        """Busca cotações por data"""
        return self.repository.find_by_data(data)
    
    def buscar_ultima_cotacao(self, moeda: str) -> Optional[Cambio]:
        """Busca última cotação de uma moeda"""
        return self.repository.find_ultima_cotacao_moeda(moeda)
    
    def criar_cotacao(self, cambio: Cambio) -> Cambio:
        """Cria nova cotação com validações"""
        # Validações de negócio
        if cambio.taxa_venda <= cambio.taxa_compra:
            raise ValueError("Taxa de venda deve ser maior que taxa de compra")
        
        return self.repository.save(cambio)
    
    def atualizar_cotacao(self, cambio_id: int, cambio: Cambio) -> Optional[Cambio]:
        """Atualiza cotação existente"""
        existente = self.repository.find_by_id(cambio_id)
        if not existente:
            return None
        
        cambio.cambio_id = cambio_id
        return self.repository.save(cambio)
    
    def deletar_cotacao(self, cambio_id: int) -> bool:
        """Remove cotação"""
        return self.repository.delete(cambio_id)
    
    def contar_cotacoes(self) -> int:
        """Conta total de cotações"""
        return self.repository.count()
