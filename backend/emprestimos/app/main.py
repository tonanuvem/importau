"""
Aplicação FastAPI principal para microsserviço de Empréstimos
"""
import os
import csv
from datetime import datetime
from fastapi import FastAPI, Response
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from .controller import emprestimo_controller
from .repository.database import init_db, get_db
from .repository.emprestimo_repository import EmprestimoRepository
from .service.emprestimo_service import EmprestimoService
from .domain.emprestimo import Emprestimo

APP_NAME = os.getenv("APP_NAME", "emprestimos-service")
APP_VERSION = os.getenv("APP_VERSION", "1.0.0")

app = FastAPI(
    title="Empréstimos Microservice",
    description="API para gerenciamento de empréstimos",
    version=APP_VERSION,
    docs_url="/swagger-ui/index.html",
    redoc_url="/redoc",
    openapi_url="/api/openapi.json"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
    expose_headers=["X-Total-Count"]
)

app.include_router(emprestimo_controller.router)


def load_initial_data():
    """Carrega dados iniciais do CSV se não existirem"""
    db = next(get_db())
    repository = EmprestimoRepository(db)
    
    # Verifica se já existem dados
    if repository.contar_emprestimos() > 0:
        return
    
    csv_path = "/app/csv_exports/emprestimos.csv"
    if not os.path.exists(csv_path):
        return
    
    try:
        with open(csv_path, 'r', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            for row in reader:
                emprestimo = Emprestimo(
                    emprestimo_id=row['emprestimo_id'],
                    data_contratacao=row['data_contratacao'],
                    instituicao_financeira=row['instituicao_financeira'],
                    valor_principal_brl=float(row['valor_principal_brl']),
                    taxa_juros_anual=float(row['taxa_juros_anual']),
                    prazo_meses=int(row['prazo_meses']),
                    valor_parcela_mensal=float(row['valor_parcela_mensal']),
                    saldo_devedor=float(row['saldo_devedor']),
                    status=row['status'],
                    finalidade=row['finalidade'],
                    data_vencimento_proxima=row['data_vencimento_proxima'] if row['data_vencimento_proxima'] else None,
                    num_parcelas_pagas=int(row['num_parcelas_pagas']),
                    usuario_responsavel=row['usuario_responsavel']
                )
                repository.criar_emprestimo(emprestimo)
        print("✓ Dados de empréstimos carregados automaticamente")
    except Exception as e:
        print(f"✗ Erro ao carregar dados: {e}")
    finally:
        db.close()


@app.on_event("startup")
async def startup_event():
    """Inicializa banco de dados e carrega dados na inicialização"""
    init_db()
    load_initial_data()


@app.get("/api/v1/status")
@app.get("/status")
async def health_check():
    """Endpoint de health check"""
    return JSONResponse({
        "service": APP_NAME,
        "version": APP_VERSION,
        "status": "healthy",
        "timestamp": datetime.now().isoformat()
    })


@app.get("/emprestimos")
async def listar_emprestimos_compat(response: Response):
    """Endpoint de compatibilidade para /emprestimos"""
    db = next(get_db())
    repository = EmprestimoRepository(db)
    service = EmprestimoService(repository)
    
    emprestimos = service.listar_emprestimos(0, 100)
    total = service.contar_emprestimos()
    
    response.headers["X-Total-Count"] = str(total)
    return emprestimos


@app.get("/")
async def root():
    """Endpoint raiz"""
    return {
        "service": APP_NAME,
        "version": APP_VERSION,
        "docs": "/swagger-ui/index.html"
    }
