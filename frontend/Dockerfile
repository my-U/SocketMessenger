# frontend/Dockerfile
FROM node:18-alpine

WORKDIR /app

# 1. serve 설치와 의존성 설치 분리 (중요)
RUN npm install -g serve

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build

EXPOSE 3000

# 2. 절대 경로 명시 (serve 명령이 PATH에 안 잡힐 수도 있어서 안전하게 처리)
CMD ["serve", "-s", "build", "-l", "3000"]