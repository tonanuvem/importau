#!/usr/bin/env python3
"""
Teste simplificado para Swagger UI do microsserviço Pedidos
"""
import requests
import json

def test_pedidos_complete():
    """Testa completamente o serviço de pedidos"""
    
    try:
        print("🚀 Teste Completo - Microsserviço Pedidos")
        
        # 1. Testar API Status
        print("\n📡 1. Testando API Status...")
        response = requests.get("http://localhost:8002/status", timeout=10)
        assert response.status_code == 200
        status_data = response.json()
        print(f"✅ Status: {status_data['status']}")
        
        # 2. Testar GET /pedidos
        print("\n📋 2. Testando GET /pedidos...")
        response = requests.get("http://localhost:8002/pedidos", timeout=10)
        assert response.status_code == 200
        pedidos = response.json()
        assert isinstance(pedidos, list)
        print(f"✅ Retornou {len(pedidos)} pedidos")
        
        # 3. Testar GET /pedidos/{id} com primeiro pedido
        if pedidos:
            primeiro_id = pedidos[0]['id']
            print(f"\n🔍 3. Testando GET /pedidos/{primeiro_id}...")
            response = requests.get(f"http://localhost:8002/pedidos/{primeiro_id}", timeout=10)
            assert response.status_code == 200
            pedido = response.json()
            print(f"✅ Pedido encontrado: {pedido['pedido_id']}")
        
        # 4. Testar Swagger UI
        print("\n📖 4. Testando Swagger UI...")
        response = requests.get("http://localhost:8002/docs/", timeout=10)
        assert response.status_code == 200
        assert "Swagger UI" in response.text
        print("✅ Swagger UI carregando corretamente")
        
        # 5. Testar OpenAPI JSON
        print("\n📄 5. Testando OpenAPI JSON...")
        response = requests.get("http://localhost:8002/openapi.json", timeout=10)
        assert response.status_code == 200
        openapi_spec = response.json()
        assert "openapi" in openapi_spec
        print(f"✅ OpenAPI spec válido: {openapi_spec['info']['title']}")
        
        # 6. Verificar endpoints no OpenAPI
        print("\n🛣️ 6. Verificando endpoints disponíveis...")
        paths = openapi_spec.get('paths', {})
        endpoints = list(paths.keys())
        print(f"✅ Endpoints encontrados: {len(endpoints)}")
        for endpoint in endpoints[:5]:  # Mostrar primeiros 5
            print(f"   - {endpoint}")
        
        print("\n🎉 TODOS OS TESTES PASSARAM!")
        print("=" * 50)
        print("✅ API Status: OK")
        print("✅ GET /pedidos: OK")
        print("✅ GET /pedidos/{id}: OK") 
        print("✅ Swagger UI: OK")
        print("✅ OpenAPI Spec: OK")
        print("=" * 50)
        
        return True
        
    except Exception as e:
        print(f"❌ Erro no teste: {e}")
        return False

if __name__ == "__main__":
    success = test_pedidos_complete()
    exit(0 if success else 1)
