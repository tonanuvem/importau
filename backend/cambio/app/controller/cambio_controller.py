"""
Controller REST para Cambio
"""
from typing import List
from datetime import date
from fastapi import APIRouter, Depends, HTTPException, Response
from sqlalchemy.orm import Session
from ..domain.cambio import Cambio
from ..repository.database import get_db
from ..repository.cambio_repository import CambioRepository
from ..service.cambio_service import CambioService

router = APIRouter(prefix="/api/v1/cambio", tags=["cambio"])


def get_service(db: Session = Depends(get_db)) -> CambioService:
    """Dependency para obter serviço"""
    repository = CambioRepository(db)
    return CambioService(repository)


@router.get("", response_model=List[Cambio])
async def listar_cotacoes(
    response: Response,
    skip: int = 0,
    limit: int = 100,
    service: CambioService = Depends(get_service)
):
    """Lista todas as cotações com paginação"""
    cotacoes = service.listar_cotacoes(skip, limit)
    total = service.contar_cotacoes()
    
    response.headers["X-Total-Count"] = str(total)
    response.headers["Access-Control-Expose-Headers"] = "X-Total-Count"
    
    return cotacoes


@router.get("/{cambio_id}", response_model=Cambio)
async def buscar_cotacao(
    cambio_id: int,
    service: CambioService = Depends(get_service)
):
    """Busca cotação por ID"""
    cotacao = service.buscar_por_id(cambio_id)
    if not cotacao:
        raise HTTPException(status_code=404, detail="Cotação não encontrada")
    return cotacao


@router.get("/moeda/{moeda}", response_model=List[Cambio])
async def buscar_por_moeda(
    moeda: str,
    service: CambioService = Depends(get_service)
):
    """Busca cotações por moeda"""
    return service.buscar_por_moeda(moeda)


@router.get("/moeda/{moeda}/ultima", response_model=Cambio)
async def buscar_ultima_cotacao(
    moeda: str,
    service: CambioService = Depends(get_service)
):
    """Busca última cotação de uma moeda"""
    cotacao = service.buscar_ultima_cotacao(moeda)
    if not cotacao:
        raise HTTPException(status_code=404, detail="Cotação não encontrada")
    return cotacao


@router.get("/data/{data}", response_model=List[Cambio])
async def buscar_por_data(
    data: date,
    service: CambioService = Depends(get_service)
):
    """Busca cotações por data"""
    return service.buscar_por_data(data)


@router.post("", response_model=Cambio, status_code=201)
async def criar_cotacao(
    cambio: Cambio,
    service: CambioService = Depends(get_service)
):
    """Cria nova cotação"""
    try:
        return service.criar_cotacao(cambio)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))


@router.put("/{cambio_id}", response_model=Cambio)
async def atualizar_cotacao(
    cambio_id: int,
    cambio: Cambio,
    service: CambioService = Depends(get_service)
):
    """Atualiza cotação existente"""
    resultado = service.atualizar_cotacao(cambio_id, cambio)
    if not resultado:
        raise HTTPException(status_code=404, detail="Cotação não encontrada")
    return resultado


@router.delete("/{cambio_id}", status_code=204)
async def deletar_cotacao(
    cambio_id: int,
    service: CambioService = Depends(get_service)
):
    """Remove cotação"""
    if not service.deletar_cotacao(cambio_id):
        raise HTTPException(status_code=404, detail="Cotação não encontrada")
    return None
