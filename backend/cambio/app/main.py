"""
Aplicação FastAPI principal para microsserviço de Câmbio
"""
import os
from datetime import datetime
from fastapi import FastAPI, Response
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from .controller import cambio_controller
from .repository.database import init_db, get_db
from .repository.cambio_repository import CambioRepository
from .service.cambio_service import CambioService

APP_NAME = os.getenv("APP_NAME", "cambio-service")
APP_VERSION = os.getenv("APP_VERSION", "1.0.0")

app = FastAPI(
    title="Câmbio Microservice",
    description="API para gerenciamento de cotações de câmbio",
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

app.include_router(cambio_controller.router)


@app.on_event("startup")
async def startup_event():
    """Inicializa banco de dados na inicialização"""
    init_db()


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


@app.get("/cambio")
async def listar_cambio_compat(response: Response):
    """Endpoint de compatibilidade para /cambio"""
    db = next(get_db())
    repository = CambioRepository(db)
    service = CambioService(repository)
    
    cotacoes = service.listar_cotacoes(0, 100)
    total = service.contar_cotacoes()
    
    response.headers["X-Total-Count"] = str(total)
    return cotacoes


@app.get("/")
async def root():
    """Endpoint raiz"""
    return {
        "service": APP_NAME,
        "version": APP_VERSION,
        "docs": "/swagger-ui/index.html"
    }
