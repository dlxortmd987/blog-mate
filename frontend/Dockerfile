FROM node:lts-alpine as builder

WORKDIR /frontend

COPY package.json package-lock.json ./
RUN npm install
COPY . .

# Tailwind 관련 의존성 확인 및 빌드
RUN npm install -D tailwindcss postcss autoprefixer
RUN npx tailwindcss init -p
RUN npm run build

FROM nginx:alpine
COPY --from=builder /frontend/dist /usr/share/nginx/html

# 올바른 MIME 타입 설정이 포함된 nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80