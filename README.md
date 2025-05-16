# security-jwt-template
Spring Security + JWT 통합 설정 템플릿 레포지토리입니다.

이 템플릿을 사용하면 인증/인가 로직과 환경 변수 관리, CI/CD 설정까지 빠르게 구성할 수 있습니다.

## 1. 🚀 템플릿 사용 방법

1. 이 템플릿을 기반으로 새 레포지토리를 생성합니다. (Use this template)
2. 아래 순서대로 설정 파일을 수정하고, 필요한 Secret 값을 등록합니다.
3. IntelliJ 또는 Docker 환경에서 실행하거나 GitHub Actions로 자동 배포합니다.

## 2. 🛠 필수 수정 파일 및 항목

| 파일 경로                                                | 수정 위치                                                                                         | 설명                                                                                         |
|------------------------------------------------------|-----------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| `/src/main/resources/application-example.yml`        | `spring.application.name`                                                                     | 어플리케이션 이름을 프로젝트에 맞게 수정해야 합니다.                                                              |
| `/src/main/resources/application-secret-example.yml` | `schema.base`                                                                                 | 사용할 데이터베이스 스키마명을 `.env.common`의 `DATABASE=`에서 설정해야 합니다.                                    |
| 〃                                                    | `spring.datasource.url`, `username`, `password`                                               | 사용하는 DB 종류(MySQL, PostgreSQL 등)에 따라 주석을 해제하고, 해당 값들은 `.env.dev` 또는 `.env.prod`에서 설정해야 합니다. |
| `.env.common.example`                                | `DATABASE`                                                                                    | 사용할 데이터베이스 스키마명을 입력해야 합니다.                                                                 |
| 〃                                                    | `MYSQL_ROOT_PASSWORD`, `POSTGRES_PASSWORD`                                                    | Docker를 생성할 때 생성할 기본 사용자의 비밀번호를 입력해야 합니다.                                                  |
| 〃                                                    | `JWT_SECRET_KEY`                                                                              | JWT 서명을 위한 비밀 키를 반드시 입력해야 합니다.                                                             |
| `.env.dev.example`                                   | `SPRING_DATASOURCE_URL_MYSQL`, `SPRING_DATASOURCE_URL_POSTGRESQL`, `SPRING_DATASOURCE_URL_H2` | URL 내 `example`이라는 DB 이름을 실제 사용하는 DB 이름으로 수정해야 합니다.                                        |
| 〃                                                    | `SPRING_DATASOURCE_USERNAME_MYSQL`, `SPRING_DATASOURCE_PASSWORD_MYSQL`                        | 기본(root) 계정이 아닌, init.sql에 생성한 사용자명과 비밀번호를 입력해야 합니다.                                       |
| 〃                                                    | `SPRING_DATASOURCE_USERNAME_POSTGRESQL`, `SPRING_DATASOURCE_PASSWORD_POSTGRESQL`              | 기본(postgres) 계정이 아닌, init.sql에 생성한 사용자명과 비밀번호를 입력해야 합니다.                                   |
| 〃                                                    | `SPRING_DATASOURCE_USERNAME_H2`, `SPRING_DATASOURCE_PASSWORD_H2`                              | 로컬 H2 DB 사용자명과 비밀번호를 입력해야 합니다.                                                             |
| `.env.prod.example`                                  | `SPRING_DATASOURCE_URL_MYSQL`, `SPRING_DATASOURCE_URL_POSTGRESQL`                             | URL 내 `example`이라는 DB 이름을 실제 사용하는 DB 이름으로 수정해야 합니다.                                        |
| 〃                                                    | `SPRING_DATASOURCE_USERNAME_MYSQL`, `SPRING_DATASOURCE_PASSWORD_MYSQL`, `MYSQL_ROOT_PASSWORD` | 기본(root) 계정이 아닌, init.sql에 생성한 운영 MySQL 사용자명, 비밀번호를 반드시 입력해야 합니다.                 |
| 〃                                                    | `SPRING_DATASOURCE_USERNAME_POSTGRESQL`, `SPRING_DATASOURCE_PASSWORD_POSTGRESQL`              | 기본(postgres) 계정이 아닌, init.sql에 생성한 운영 PostgreSQL 사용자명과 비밀번호를 입력해야 합니다.                                                        |



---

## 3. 📦 사용 기술 스택

### 언어 및 환경
- Java 21
- Spring Boot 3.4.3
- Gradle (Groovy DSL)

### 주요 라이브러리
| 라이브러리 | 버전 |
|-----------|------|
| Auth0 Java JWT | 4.4.0 |
| SpringDoc OpenAPI (Swagger) | 2.3.0 |
| MapStruct | 1.5.5.Final |
| Lombok | 최신 (Spring BOM에 의해 관리) |
| Spring Security | Spring Boot BOM 기준 |
| Redis | spring-boot-starter-data-redis |
| MySQL Connector | spring-boot-starter-jdbc 기준 |

---

## 4. 환경 변수 설정 안내 (.env 파일)

- `.env.common.example`, `.env.local.example`, `.env.prod.example`은 예시 파일입니다.
- 실행 환경에 따라 `.env` 병합이 필요할 수 있습니다.

### 실행 환경별 .env 동작 방식

| 환경 | 자동 로드 여부 | 설명                                                            |
|------|----------------|---------------------------------------------------------------|
| **IntelliJ (Spring Boot)** | ❌ 수동 등록 필요 | Run 설정에서 `.env` 지정                                            |
| **Vite / Node.js** | ✅ 자동 로드 | `.env`, `.env.local`, `.env.production` 자동 인식                 |
| **Docker Compose** | ✅ `.env`만 자동 | Docker 내부에서 환경 변수가 사용될 경우 `.env.common` 등은 `env_file:`로 명시 필요 |

---

### 사용 권장 방식

1. **Vite** 등의 자동 로드 환경에서는 `.env` 또는 `.env.local`, `.env.prod`를 파일명에 맞게 사용하세요.
2. **Docker** 등의 환경에서는 .env 파일만 자동으로 로드됩니다.  
   **.env.dev, .env.prod, .env.common** 등의 파일은 자동으로 로드되지 않으므로,
   **docker-compose** 파일에서 환경 변수가 사용될 경우 실행 전에 수동으로 .env 파일로 병합해야 합니다.  
   Docker 내부에서 환경 변수가 사용될 경우 **env_file:** 을 통해 명시적으로 지정해야 합니다.
```bash
# 예시: 실행 전에 두 파일을 병합
cat .env.common .env.dev > .env
```
3. **IntelliJ에서 실행할 경우**, 실행 설정에서 `.env` 파일을 지정해야 환경변수가 적용됩니다.
> ⚠️ `.env.dev`만 등록하면 `.env.common`의 값은 자동 적용되지 않으므로 반드시 병합하거나 명시적으로 둘 다 등록해야 합니다.

---

## 5. 🔐 GitHub Actions Secrets 설정 가이드

| 이름 | 설명 |
|------|------|
| `ENV_COMMON_FILE` | `.env.common` 파일을 base64 인코딩한 문자열 |
| `ENV_PROD_FILE` | `.env.prod` 파일을 base64 인코딩한 문자열 |
| `SECRET_FILE` | `application-secret.yml` 파일을 base64 인코딩한 문자열 |
| `DOCKER_HUB_USERNAME` | Docker Hub 사용자 이름 |
| `DOCKER_HUB_PASSWORD` | Docker Hub 비밀번호 |
| `SERVER_HOST` | 배포 대상 서버의 IP 주소 |
| `SERVER_USER` | 배포 서버의 사용자 이름 (예: ubuntu) |
| `SERVER_SSH_KEY` | 배포 서버에 접속할 SSH 개인 키 (OpenSSH 포맷) |
| `SSH_PORT` | SSH 접속 포트 (기본값: 22) |

```bash
# 예시: .env.common 파일을 base64로 인코딩한 문자열 출력
base64 .env.common > env_common.b64
```

---

## 6. Deploy 설정 안내 (GitHub Actions)

| 항목 | 설명                                                            |
|------|---------------------------------------------------------------|
| `tags: your-dockerhub-username/your-image-name:latest` | Docker Hub에 푸시할 이미지의 사용자명과 이미지명을 본인의 계정 정보에 맞게 수정해야 합니다.      |
| `cd ~/your-project-directory` | 배포 서버에서 docker-compose.yml와 .env 파일을 저장하고 실행할 디렉토리로 수정해야 합니다. |
| `curl -o docker-compose.yml https://raw.githubusercontent.com/your-username/your-repo-name/main/docker-compose.yml` | 프로젝트의 GitHub 사용자명, 레포지토리명, 브랜치명에 맞게 수정해야 합니다.                 |
| `docker pull your-dockerhub-username/your-image-name:latest` | Docker Hub 사용자명과 이미지명을 본인에게 맞게 수정해야 합니다.                      |

> 📝 이 항목들은 GitHub Actions의 `deploy` 스크립트 내에서 직접 사용하는 명령어이므로,  
> 템플릿을 기반으로 만든 레포지토리에 맞게 반드시 수정해야 합니다.

> 🔥 모든 민감한 값은 Git에 커밋되지 않도록 `.gitignore` 파일을 확인하세요.



