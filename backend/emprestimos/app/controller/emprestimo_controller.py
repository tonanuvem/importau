"""
Controller REST para Emprestimos
Endpoints HTTP seguindo padrão react-admin
"""
from typing import List
from fastapi import APIRouter, Depends, HTTPException, Response
from sqlalchemy.orm import Session
from ..domain.emprestimo import Emprestimo, StatusEmprestimo
from ..repository.database import get_db
from ..repository.emprestimo_repository import EmprestimoRepository
from ..service.emprestimo_service import EmprestimoService

router = APIRouter(prefix="/api/v1/emprestimos", tags=["emprestimos"])


def get_service(db: Session = Depends(get_db)) -> EmprestimoService:
    """Dependency para obter serviço"""
    repository = EmprestimoRepository(db)
    return EmprestimoService(repository)


@router.get("", response_model=List[Emprestimo])
async def listar_emprestimos(
    response: Response,
    skip: int = 0,
    limit: int = 100,
    service: EmprestimoService = Depends(get_service)
):
    """Lista todos os empréstimos com paginação"""
    emprestimos = service.listar_emprestimos(skip, limit)
    total = service.contar_emprestimos()
    
    # Header para react-admin
    response.headers["X-Total-Count"] = str(total)
    response.headers["Access-Control-Expose-Headers"] = "X-Total-Count"
    
    return emprestimos


@router.get("/{emprestimo_id}", response_model=Emprestimo)
async def buscar_emprestimo(
    emprestimo_id: str,
    service: EmprestimoService = Depends(get_service)
):
    """Busca empréstimo por ID"""
    emprestimo = service.buscar_por_id(emprestimo_id)
    if not emprestimo:
        raise HTTPException(status_code=404, detail="Empréstimo não encontrado")
    return emprestimo


@router.get("/status/{status}", response_model=List[Emprestimo])
async def buscar_por_status(
    status: StatusEmprestimo,
    service: EmprestimoService = Depends(get_service)
):
    """Busca empréstimos por status"""
    return service.buscar_por_status(status)


@router.get("/instituicao/{instituicao}", response_model=List[Emprestimo])
async def buscar_por_instituicao(
    instituicao: str,
    service: EmprestimoService = Depends(get_service)
):
    """Busca empréstimos por instituição financeira"""
    return service.buscar_por_instituicao(instituicao)


@router.post("", response_model=Emprestimo, status_code=201)
async def criar_emprestimo(
    emprestimo: Emprestimo,
    service: EmprestimoService = Depends(get_service)
):
    """Cria novo empréstimo"""
    try:
        return service.criar_emprestimo(emprestimo)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))


@router.put("/{emprestimo_id}", response_model=Emprestimo)
async def atualizar_emprestimo(
    emprestimo_id: str,
    emprestimo: Emprestimo,
    service: EmprestimoService = Depends(get_service)
):
    """Atualiza empréstimo existente"""
    resultado = service.atualizar_emprestimo(emprestimo_id, emprestimo)
    if not resultado:
        raise HTTPException(status_code=404, detail="Empréstimo não encontrado")
    return resultado


@router.delete("/{emprestimo_id}", status_code=204)
async def deletar_emprestimo(
    emprestimo_id: str,
    service: EmprestimoService = Depends(get_service)
):
    """Remove empréstimo"""
    if not service.deletar_emprestimo(emprestimo_id):
        raise HTTPException(status_code=404, detail="Empréstimo não encontrado")
    return None
