# 1. Node.js 기반에서 Angular 빌드
FROM node:18 AS build

WORKDIR /app

# package.json과 package-lock.json 복사 후 의존성 설치
COPY package*.json ./
RUN npm install

# 소스 코드 복사 후 빌드 실행
COPY . .
RUN npm run build --prod

# 2. Nginx를 사용한 정적 파일 서빙
FROM nginx:alpine AS runtime

# Nginx 설정 파일을 복사 (필요하면 별도 설정 추가 가능)
COPY nginx.conf /etc/nginx/nginx.conf

# 빌드된 Angular 애플리케이션을 Nginx의 기본 웹 디렉토리에 복사
COPY --from=build /app/dist/first-app /usr/share/nginx/html

# 컨테이너가 실행될 때 Nginx를 시작하도록 설정
CMD ["nginx", "-g", "daemon off;"]

# 컨테이너에서 노출할 포트 설정
EXPOSE 80
