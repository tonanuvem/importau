FROM node:18-alpine

# Configurações do ambiente
WORKDIR /app
ENV NODE_ENV=production

# Instala dependências do sistema
RUN apk add --no-cache curl

# Copia package.json e instala dependências
COPY package*.json ./
RUN npm install --only=production

# Copia código da aplicação
COPY . .

# Expõe porta
EXPOSE 8002

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8002/status || exit 1

# Comando para iniciar aplicação
CMD ["node", "server.js"]
