"""
Script para migrar dados do CSV para o banco de dados
"""
import csv
import requests

API_URL = "http://cambio-service:8086/api/v1/cambio"
CSV_PATH = "/home/ubuntu/environment/aidev/openfinance/csv_exports/cambio.csv"

def migrate_data():
    """Migra dados do CSV para a API"""
    with open(CSV_PATH, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        count = 0
        
        for row in reader:
            cambio = {
                "data_cotacao": row['data_cotacao'],
                "moeda": row['moeda'],
                "taxa_compra": float(row['taxa_compra']),
                "taxa_venda": float(row['taxa_venda']),
                "taxa_ptax": float(row['taxa_ptax']),
                "variacao_dia_percentual": float(row['variacao_dia_percentual']),
                "fonte": row['fonte'],
                "tipo_cambio": row['tipo_cambio'],
                "hora_atualizacao": row['hora_atualizacao']
            }
            
            try:
                response = requests.post(API_URL, json=cambio)
                if response.status_code == 201:
                    count += 1
                    print(f"✓ Cotação {cambio['moeda']} {cambio['data_cotacao']} criada")
                else:
                    print(f"✗ Erro: {response.text}")
            except Exception as e:
                print(f"✗ Erro: {str(e)}")
        
        print(f"\n{count} cotações migradas com sucesso!")

if __name__ == "__main__":
    migrate_data()
