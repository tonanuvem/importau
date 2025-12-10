FROM python:3.11-slim

WORKDIR /app

# Instala dependências do sistema
RUN apt-get update && apt-get install -y \
    gcc \
    postgresql-client \
    && rm -rf /var/lib/apt/lists/*

# Copia requirements e instala dependências Python
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia código da aplicação
COPY app ./app
COPY tests ./tests

# Expõe porta da aplicação
EXPOSE 8085

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD python -c "import urllib.request; urllib.request.urlopen('http://localhost:8085/api/v1/status')" || exit 1

# Comando para iniciar aplicação
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8085"]
