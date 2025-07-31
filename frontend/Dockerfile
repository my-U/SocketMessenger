# frontend/Dockerfile
FROM node:18-alpine

WORKDIR /app
COPY package*.json ./

# serve 설치 포함 (글로벌 설치)
RUN npm install -g serve && npm install

COPY . .
RUN npm run build

EXPOSE 3000
CMD ["serve", "-s", "build", "-l", "3000"]