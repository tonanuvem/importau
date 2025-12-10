"""
Microsserviço de Produtos - IMPORTAÚ Open Finance
Implementa CRUD de produtos com arquitetura hexagonal
"""
from fastapi import FastAPI, HTTPException, Depends, Query
from fastapi.middleware.cors import CORSMiddleware
from fastapi.security import HTTPBearer
from sqlalchemy import create_engine, Column, String, Float, Integer, Boolean, DateTime
from sqlalchemy.orm import sessionmaker, Session, DeclarativeBase
from pydantic import BaseModel
from typing import List, Optional
import os
import uuid
from datetime import datetime
import csv

# Configurações via variáveis de ambiente
DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://postgres:postgres@localhost:5432/produtos_db")
HOST = os.getenv("HOST", "0.0.0.0")
PORT = int(os.getenv("PORT", "8001"))

# Configuração do banco de dados
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

class Base(DeclarativeBase):
    pass

# Modelo de dados (Entity)
class ProdutoEntity(Base):
    __tablename__ = "produtos"
    
    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    codigo = Column(String(50), unique=True, nullable=False)
    nome = Column(String(200), nullable=False)
    categoria = Column(String(100), nullable=False)
    subcategoria = Column(String(100))
    fornecedor_id = Column(String(50))
    preco_unitario_brl = Column(Float, nullable=False)
    moeda_origem = Column(String(3), default="BRL")
    preco_origem = Column(Float)
    unidade_medida = Column(String(20), default="UN")
    peso_kg = Column(Float, default=0.0)
    estoque_atual = Column(Integer, default=0)
    estoque_minimo = Column(Integer, default=0)
    lead_time_dias = Column(Integer, default=30)
    origem_fabricacao = Column(String(100))
    ativo = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

# Modelos Pydantic (Domain Models)
class ProdutoBase(BaseModel):
    codigo: str
    nome: str
    categoria: str
    subcategoria: Optional[str] = None
    fornecedor_id: Optional[str] = None
    preco_unitario_brl: float
    moeda_origem: str = "BRL"
    preco_origem: Optional[float] = None
    unidade_medida: str = "UN"
    peso_kg: float = 0.0
    estoque_atual: int = 0
    estoque_minimo: int = 0
    lead_time_dias: int = 30
    origem_fabricacao: Optional[str] = None
    ativo: bool = True

class ProdutoCreate(ProdutoBase):
    pass

class ProdutoUpdate(BaseModel):
    nome: Optional[str] = None
    categoria: Optional[str] = None
    subcategoria: Optional[str] = None
    preco_unitario_brl: Optional[float] = None
    estoque_atual: Optional[int] = None
    estoque_minimo: Optional[int] = None
    ativo: Optional[bool] = None

class Produto(ProdutoBase):
    id: str
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True

# Repository (Data Layer)
class ProdutoRepository:
    def __init__(self, db: Session):
        self.db = db
    
    def get_all(self, skip: int = 0, limit: int = 100, categoria: str = None):
        query = self.db.query(ProdutoEntity)
        if categoria:
            query = query.filter(ProdutoEntity.categoria == categoria)
        return query.offset(skip).limit(limit).all()
    
    def get_by_id(self, produto_id: str):
        return self.db.query(ProdutoEntity).filter(ProdutoEntity.id == produto_id).first()
    
    def get_by_codigo(self, codigo: str):
        return self.db.query(ProdutoEntity).filter(ProdutoEntity.codigo == codigo).first()
    
    def create(self, produto: ProdutoCreate):
        db_produto = ProdutoEntity(**produto.model_dump())
        self.db.add(db_produto)
        self.db.commit()
        self.db.refresh(db_produto)
        return db_produto
    
    def update(self, produto_id: str, produto_update: ProdutoUpdate):
        db_produto = self.get_by_id(produto_id)
        if not db_produto:
            return None
        
        for field, value in produto_update.model_dump(exclude_unset=True).items():
            setattr(db_produto, field, value)
        
        db_produto.updated_at = datetime.utcnow()
        self.db.commit()
        self.db.refresh(db_produto)
        return db_produto
    
    def delete(self, produto_id: str):
        db_produto = self.get_by_id(produto_id)
        if db_produto:
            self.db.delete(db_produto)
            self.db.commit()
        return db_produto

# Service (Business Layer)
class ProdutoService:
    def __init__(self, repository: ProdutoRepository):
        self.repository = repository
    
    def listar_produtos(self, skip: int = 0, limit: int = 100, categoria: str = None):
        return self.repository.get_all(skip, limit, categoria)
    
    def obter_produto(self, produto_id: str):
        produto = self.repository.get_by_id(produto_id)
        if not produto:
            raise HTTPException(status_code=404, detail="Produto não encontrado")
        return produto
    
    def criar_produto(self, produto: ProdutoCreate):
        # Validação de negócio: código único
        if self.repository.get_by_codigo(produto.codigo):
            raise HTTPException(status_code=400, detail="Código do produto já existe")
        
        return self.repository.create(produto)
    
    def atualizar_produto(self, produto_id: str, produto_update: ProdutoUpdate):
        produto = self.repository.update(produto_id, produto_update)
        if not produto:
            raise HTTPException(status_code=404, detail="Produto não encontrado")
        return produto
    
    def excluir_produto(self, produto_id: str):
        produto = self.repository.delete(produto_id)
        if not produto:
            raise HTTPException(status_code=404, detail="Produto não encontrado")
        return {"message": "Produto excluído com sucesso"}

# Dependency Injection
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def get_produto_service(db: Session = Depends(get_db)):
    repository = ProdutoRepository(db)
    return ProdutoService(repository)

# FastAPI App
app = FastAPI(
    title="Microsserviço de Produtos",
    description="API para gestão de produtos - IMPORTAÚ Open Finance",
    version="1.0.0"
)

# CORS Configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Security
security = HTTPBearer(auto_error=False)

# Controllers (API Layer)
@app.get("/status")
async def health_check():
    """Endpoint de verificação de saúde do serviço"""
    return {"status": "healthy", "service": "produtos", "timestamp": datetime.utcnow()}

@app.get("/produtos", response_model=List[Produto])
async def listar_produtos(
    skip: int = Query(0, ge=0),
    limit: int = Query(100, ge=1, le=1000),
    categoria: Optional[str] = Query(None),
    service: ProdutoService = Depends(get_produto_service)
):
    """Lista produtos com filtros opcionais"""
    return service.listar_produtos(skip, limit, categoria)

@app.get("/produtos/{produto_id}", response_model=Produto)
async def obter_produto(
    produto_id: str,
    service: ProdutoService = Depends(get_produto_service)
):
    """Obtém produto por ID"""
    return service.obter_produto(produto_id)

@app.post("/produtos", response_model=Produto, status_code=201)
async def criar_produto(
    produto: ProdutoCreate,
    service: ProdutoService = Depends(get_produto_service)
):
    """Cria novo produto"""
    return service.criar_produto(produto)

@app.put("/produtos/{produto_id}", response_model=Produto)
async def atualizar_produto(
    produto_id: str,
    produto_update: ProdutoUpdate,
    service: ProdutoService = Depends(get_produto_service)
):
    """Atualiza produto existente"""
    return service.atualizar_produto(produto_id, produto_update)

@app.delete("/produtos/{produto_id}")
async def excluir_produto(
    produto_id: str,
    service: ProdutoService = Depends(get_produto_service)
):
    """Exclui produto"""
    return service.excluir_produto(produto_id)

# Inicialização do banco de dados
def init_db():
    """Cria tabelas no banco de dados"""
    Base.metadata.create_all(bind=engine)

def load_csv_data():
    """Carrega dados do CSV para o banco"""
    db = SessionLocal()
    try:
        # Verifica se já existem dados
        if db.query(ProdutoEntity).count() > 0:
            print("Dados já existem no banco")
            return
        
        csv_path = "/app/csv_exports/produtos.csv"
        if not os.path.exists(csv_path):
            print(f"Arquivo CSV não encontrado: {csv_path}")
            return
        
        with open(csv_path, 'r', encoding='utf-8') as file:
            reader = csv.DictReader(file)
            for row in reader:
                produto = ProdutoEntity(
                    codigo=row['produto_id'],
                    nome=row['nome_produto'],
                    categoria=row['categoria'],
                    subcategoria=row['subcategoria'],
                    fornecedor_id=row['fornecedor_id'],
                    preco_unitario_brl=float(row['preco_unitario_brl']),
                    moeda_origem=row['moeda_origem'],
                    preco_origem=float(row['preco_origem']),
                    unidade_medida=row['unidade_medida'],
                    peso_kg=float(row['peso_kg']),
                    estoque_atual=int(row['estoque_atual']),
                    estoque_minimo=int(row['estoque_minimo']),
                    lead_time_dias=int(row['lead_time_dias']),
                    origem_fabricacao=row['origem_fabricacao']
                )
                db.add(produto)
        
        db.commit()
        print("Dados do CSV carregados com sucesso")
    
    except Exception as e:
        print(f"Erro ao carregar dados do CSV: {e}")
        db.rollback()
    finally:
        db.close()

if __name__ == "__main__":
    import uvicorn
    
    # Inicializa banco e dados
    init_db()
    load_csv_data()
    
    # Inicia servidor
    uvicorn.run(app, host=HOST, port=PORT)
