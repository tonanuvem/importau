"""
Testes unitários para o microsserviço de produtos
Implementa TDD com cobertura completa
"""
import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from main import app, get_db, Base, ProdutoEntity, ProdutoService, ProdutoRepository
import uuid

# Configuração do banco de teste em memória
SQLALCHEMY_DATABASE_URL = "sqlite:///./test.db"
engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False})
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

def override_get_db():
    try:
        db = TestingSessionLocal()
        yield db
    finally:
        db.close()

app.dependency_overrides[get_db] = override_get_db

# Setup do banco de teste
@pytest.fixture(scope="function", autouse=True)
def setup_database():
    Base.metadata.drop_all(bind=engine)
    Base.metadata.create_all(bind=engine)
    yield
    Base.metadata.drop_all(bind=engine)

@pytest.fixture
def client():
    return TestClient(app)

@pytest.fixture
def db_session():
    from main import Base
    Base.metadata.create_all(bind=engine)
    db = TestingSessionLocal()
    try:
        yield db
    finally:
        db.close()

# Dados de teste
@pytest.fixture
def produto_data():
    return {
        "codigo": "TEST001",
        "nome": "Produto Teste",
        "categoria": "Teste",
        "subcategoria": "Subcategoria Teste",
        "fornecedor_id": "FORN001",
        "preco_unitario_brl": 100.0,
        "moeda_origem": "BRL",
        "preco_origem": 100.0,
        "unidade_medida": "UN",
        "peso_kg": 1.0,
        "estoque_atual": 50,
        "estoque_minimo": 10,
        "lead_time_dias": 15,
        "origem_fabricacao": "Brasil",
        "ativo": True
    }

# Testes da camada Repository
class TestProdutoRepository:
    
    def test_create_produto(self, db_session, produto_data):
        """Teste de criação de produto no repositório"""
        from main import ProdutoCreate
        
        repo = ProdutoRepository(db_session)
        produto_create = ProdutoCreate(**produto_data)
        
        produto = repo.create(produto_create)
        
        assert produto.codigo == produto_data["codigo"]
        assert produto.nome == produto_data["nome"]
        assert produto.categoria == produto_data["categoria"]
        assert produto.id is not None
    
    def test_get_by_id(self, db_session, produto_data):
        """Teste de busca por ID"""
        repo = ProdutoRepository(db_session)
        
        # Cria produto
        produto_entity = ProdutoEntity(**produto_data)
        db_session.add(produto_entity)
        db_session.commit()
        
        # Busca por ID
        found_produto = repo.get_by_id(produto_entity.id)
        
        assert found_produto is not None
        assert found_produto.codigo == produto_data["codigo"]
    
    def test_get_by_codigo(self, db_session, produto_data):
        """Teste de busca por código"""
        repo = ProdutoRepository(db_session)
        
        # Cria produto
        produto_entity = ProdutoEntity(**produto_data)
        db_session.add(produto_entity)
        db_session.commit()
        
        # Busca por código
        found_produto = repo.get_by_codigo(produto_data["codigo"])
        
        assert found_produto is not None
        assert found_produto.nome == produto_data["nome"]
    
    def test_get_all_with_filter(self, db_session):
        """Teste de listagem com filtro por categoria"""
        repo = ProdutoRepository(db_session)
        
        # Cria produtos de diferentes categorias
        produto1 = ProdutoEntity(codigo="P1", nome="Produto 1", categoria="Cat1", preco_unitario_brl=100.0)
        produto2 = ProdutoEntity(codigo="P2", nome="Produto 2", categoria="Cat2", preco_unitario_brl=200.0)
        
        db_session.add_all([produto1, produto2])
        db_session.commit()
        
        # Busca por categoria
        produtos_cat1 = repo.get_all(categoria="Cat1")
        
        assert len(produtos_cat1) == 1
        assert produtos_cat1[0].categoria == "Cat1"

# Testes da camada Service
class TestProdutoService:
    
    def test_criar_produto_sucesso(self, db_session, produto_data):
        """Teste de criação de produto via service"""
        from main import ProdutoCreate
        
        repo = ProdutoRepository(db_session)
        service = ProdutoService(repo)
        produto_create = ProdutoCreate(**produto_data)
        
        produto = service.criar_produto(produto_create)
        
        assert produto.codigo == produto_data["codigo"]
        assert produto.nome == produto_data["nome"]
    
    def test_criar_produto_codigo_duplicado(self, db_session, produto_data):
        """Teste de validação de código duplicado"""
        from main import ProdutoCreate
        from fastapi import HTTPException
        
        repo = ProdutoRepository(db_session)
        service = ProdutoService(repo)
        produto_create = ProdutoCreate(**produto_data)
        
        # Cria primeiro produto
        service.criar_produto(produto_create)
        
        # Tenta criar produto com mesmo código
        with pytest.raises(HTTPException) as exc_info:
            service.criar_produto(produto_create)
        
        assert exc_info.value.status_code == 400
        assert "já existe" in exc_info.value.detail
    
    def test_obter_produto_inexistente(self, db_session):
        """Teste de busca de produto inexistente"""
        from fastapi import HTTPException
        
        repo = ProdutoRepository(db_session)
        service = ProdutoService(repo)
        
        with pytest.raises(HTTPException) as exc_info:
            service.obter_produto("id_inexistente")
        
        assert exc_info.value.status_code == 404

# Testes da API (Controllers)
class TestProdutoAPI:
    
    def test_health_check(self, client):
        """Teste do endpoint de saúde"""
        response = client.get("/status")
        
        assert response.status_code == 200
        data = response.json()
        assert data["status"] == "healthy"
        assert data["service"] == "produtos"
    
    def test_criar_produto_api(self, client, produto_data):
        """Teste de criação via API"""
        response = client.post("/produtos", json=produto_data)
        
        assert response.status_code == 201
        data = response.json()
        assert data["codigo"] == produto_data["codigo"]
        assert data["nome"] == produto_data["nome"]
        assert "id" in data
    
    def test_listar_produtos_api(self, client, produto_data):
        """Teste de listagem via API"""
        # Cria produto primeiro
        client.post("/produtos", json=produto_data)
        
        # Lista produtos
        response = client.get("/produtos")
        
        assert response.status_code == 200
        data = response.json()
        assert isinstance(data, list)
        assert len(data) >= 1
    
    def test_obter_produto_api(self, client, produto_data):
        """Teste de busca por ID via API"""
        # Cria produto
        create_response = client.post("/produtos", json=produto_data)
        produto_id = create_response.json()["id"]
        
        # Busca produto
        response = client.get(f"/produtos/{produto_id}")
        
        assert response.status_code == 200
        data = response.json()
        assert data["codigo"] == produto_data["codigo"]
    
    def test_atualizar_produto_api(self, client, produto_data):
        """Teste de atualização via API"""
        # Cria produto
        create_response = client.post("/produtos", json=produto_data)
        produto_id = create_response.json()["id"]
        
        # Atualiza produto
        update_data = {"nome": "Produto Atualizado", "preco_unitario_brl": 150.0}
        response = client.put(f"/produtos/{produto_id}", json=update_data)
        
        assert response.status_code == 200
        data = response.json()
        assert data["nome"] == "Produto Atualizado"
        assert data["preco_unitario_brl"] == 150.0
    
    def test_excluir_produto_api(self, client, produto_data):
        """Teste de exclusão via API"""
        # Cria produto
        create_response = client.post("/produtos", json=produto_data)
        produto_id = create_response.json()["id"]
        
        # Exclui produto
        response = client.delete(f"/produtos/{produto_id}")
        
        assert response.status_code == 200
        assert "excluído com sucesso" in response.json()["message"]
        
        # Verifica se foi excluído
        get_response = client.get(f"/produtos/{produto_id}")
        assert get_response.status_code == 404
    
    def test_filtro_por_categoria(self, client):
        """Teste de filtro por categoria"""
        # Cria produtos de diferentes categorias
        produto1 = {
            "codigo": "FILT001",
            "nome": "Produto Filtro 1",
            "categoria": "Eletrônicos",
            "preco_unitario_brl": 100.0
        }
        produto2 = {
            "codigo": "FILT002", 
            "nome": "Produto Filtro 2",
            "categoria": "Industrial",
            "preco_unitario_brl": 200.0
        }
        
        client.post("/produtos", json=produto1)
        client.post("/produtos", json=produto2)
        
        # Filtra por categoria
        response = client.get("/produtos?categoria=Eletrônicos")
        
        assert response.status_code == 200
        data = response.json()
        assert all(p["categoria"] == "Eletrônicos" for p in data)

# Testes de Performance
class TestPerformance:
    
    def test_response_time_limit(self, client):
        """Teste de tempo de resposta"""
        import time
        
        start_time = time.time()
        response = client.get("/produtos")
        end_time = time.time()
        
        response_time = end_time - start_time
        
        assert response.status_code == 200
        assert response_time < 3.0  # Requisito: < 3 segundos

if __name__ == "__main__":
    pytest.main(["-v", __file__])
