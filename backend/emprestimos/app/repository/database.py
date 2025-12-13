"""
Configuração de conexão com banco de dados PostgreSQL
"""
import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from .models import Base

# Configurações do banco de dados via variáveis de ambiente
DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "postgresql://postgres:postgres@emprestimos-db:5432/emprestimos_db"
)

# Engine do SQLAlchemy
engine = create_engine(DATABASE_URL, pool_pre_ping=True)

# Session factory
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)


def get_db():
    """Dependency para obter sessão do banco de dados"""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def init_db():
    """Inicializa o banco de dados criando as tabelas"""
    Base.metadata.create_all(bind=engine)
