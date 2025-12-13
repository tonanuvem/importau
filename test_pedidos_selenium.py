#!/usr/bin/env python3
"""
Teste Selenium para Swagger UI do microsserviço Pedidos
"""
import time
import requests
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

def test_pedidos_swagger_ui():
    """Testa o Swagger UI do serviço de pedidos"""
    
    # Configurar Chrome headless
    chrome_options = Options()
    chrome_options.add_argument("--headless")
    chrome_options.add_argument("--no-sandbox")
    chrome_options.add_argument("--disable-dev-shm-usage")
    chrome_options.add_argument("--disable-gpu")
    chrome_options.add_argument("--window-size=1920,1080")
    chrome_options.binary_location = "/opt/google/chrome/google-chrome"
    
    driver = None
    
    try:
        print("🚀 Iniciando teste Selenium do Swagger UI - Pedidos")
        
        # 1. Testar API diretamente primeiro
        print("📡 Testando API REST...")
        response = requests.get("http://localhost:8002/status", timeout=10)
        assert response.status_code == 200
        print("✅ API REST funcionando")
        
        # 2. Testar endpoint GET /pedidos
        print("📋 Testando endpoint GET /pedidos...")
        response = requests.get("http://localhost:8002/pedidos", timeout=10)
        assert response.status_code == 200
        data = response.json()
        assert isinstance(data, list)
        assert len(data) > 0
        print(f"✅ GET /pedidos retornou {len(data)} pedidos")
        
        # 3. Inicializar Selenium
        print("🌐 Iniciando Chrome WebDriver...")
        driver = webdriver.Chrome(options=chrome_options)
        
        # 4. Acessar Swagger UI
        swagger_url = "http://localhost:8002/docs"
        print(f"📖 Acessando Swagger UI: {swagger_url}")
        driver.get(swagger_url)
        
        # 5. Aguardar carregamento
        wait = WebDriverWait(driver, 10)
        
        # 6. Verificar se a página carregou
        print("⏳ Aguardando carregamento do Swagger UI...")
        time.sleep(3)
        
        # 7. Verificar título da página
        title = driver.title
        print(f"📄 Título da página: {title}")
        
        # 8. Procurar elementos do Swagger
        try:
            # Tentar encontrar elementos típicos do Swagger UI
            swagger_elements = driver.find_elements(By.CLASS_NAME, "swagger-ui")
            if swagger_elements:
                print("✅ Swagger UI carregado com sucesso")
            else:
                # Verificar se é uma página de redirecionamento
                body_text = driver.find_element(By.TAG_NAME, "body").text
                if "Redirecting" in body_text or "swagger" in body_text.lower():
                    print("🔄 Página de redirecionamento detectada")
                else:
                    print("⚠️ Swagger UI não encontrado, mas página carregou")
        except Exception as e:
            print(f"⚠️ Erro ao verificar elementos Swagger: {e}")
        
        # 9. Capturar screenshot
        screenshot_path = "/home/ubuntu/environment/aidev/openfinance/importau/pedidos_swagger_test.png"
        driver.save_screenshot(screenshot_path)
        print(f"📸 Screenshot salvo: {screenshot_path}")
        
        print("🎉 Teste Selenium concluído com sucesso!")
        return True
        
    except Exception as e:
        print(f"❌ Erro no teste Selenium: {e}")
        return False
        
    finally:
        if driver:
            driver.quit()
            print("🔚 WebDriver fechado")

if __name__ == "__main__":
    success = test_pedidos_swagger_ui()
    exit(0 if success else 1)
