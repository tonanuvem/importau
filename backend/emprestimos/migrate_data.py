"""
Script para migrar dados do CSV para o banco de dados
"""
import csv
import requests
from datetime import datetime
from decimal import Decimal

# URL da API
API_URL = "http://emprestimos-service:8085/api/v1/emprestimos"

# Caminho do CSV
CSV_PATH = "/home/ubuntu/environment/aidev/openfinance/csv_exports/emprestimos.csv"

def migrate_data():
    """Migra dados do CSV para a API"""
    with open(CSV_PATH, 'r', encoding='utf-8') as file:
        reader = csv.DictReader(file)
        count = 0
        
        for row in reader:
            emprestimo = {
                "emprestimo_id": row['emprestimo_id'],
                "data_contratacao": row['data_contratacao'],
                "instituicao_financeira": row['instituicao_financeira'],
                "valor_principal_brl": float(row['valor_principal_brl']),
                "taxa_juros_anual": float(row['taxa_juros_anual']),
                "prazo_meses": int(row['prazo_meses']),
                "valor_parcela_mensal": float(row['valor_parcela_mensal']),
                "saldo_devedor": float(row['saldo_devedor']),
                "status": row['status'],
                "finalidade": row['finalidade'],
                "data_vencimento_proxima": row['data_vencimento_proxima'] if row['data_vencimento_proxima'] else None,
                "num_parcelas_pagas": int(row['num_parcelas_pagas']),
                "usuario_responsavel": row['usuario_responsavel']
            }
            
            try:
                response = requests.post(API_URL, json=emprestimo)
                if response.status_code == 201:
                    count += 1
                    print(f"✓ Empréstimo {emprestimo['emprestimo_id']} criado com sucesso")
                else:
                    print(f"✗ Erro ao criar {emprestimo['emprestimo_id']}: {response.text}")
            except Exception as e:
                print(f"✗ Erro ao processar {emprestimo['emprestimo_id']}: {str(e)}")
        
        print(f"\n{count} empréstimos migrados com sucesso!")

if __name__ == "__main__":
    migrate_data()
