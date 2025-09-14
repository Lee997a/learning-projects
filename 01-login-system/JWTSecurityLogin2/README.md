# 🔐 Spring Security JWT Authentication System

Spring Boot와 Spring Security를 활용한 JWT 기반 인증 시스템입니다. RESTful API와 Thymeleaf 기반 웹 페이지를 모두 지원하는 하이브리드 구조로 설계되었습니다.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-blue)
![JWT](https://img.shields.io/badge/JWT-0.12.3-purple)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.x-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## 🌟 주요 특징

### 🔑 **이중 인증 시스템**

- **REST API**: JWT Bearer 토큰 인증 (`Authorization: Bearer <token>`)
- **웹 페이지**: HttpOnly 쿠키 기반 JWT 인증 (XSS 방지)

### 🛡️ **강력한 보안**

- **JWT HS512** 알고리즘 (512비트 이상 시크릿 키)
- **BCrypt** 비밀번호 암호화
- **무상태(Stateless)** 인증
- **HttpOnly 쿠키**로 XSS 공격 방지

### 👥 **역할 기반 접근 제어**

- **USER**: 일반 사용자 권한
- **ADMIN**: 관리자 권한 (첫 번째 회원가입 사용자)

### 🎨 **현대적인 UI/UX**

- **Bootstrap 5** 기반 반응형 디자인
- **FontAwesome** 아이콘
- **그라디언트** 및 **애니메이션** 효과
- **모바일 친화적** 인터페이스

## 📋 목차

- [시작하기](#-시작하기)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [API 사용법](#-api-사용법)
- [웹 페이지 사용법](#-웹-페이지-사용법)
- [설정 방법](#-설정-방법)
- [주요 기능](#-주요-기능)
- [스크린샷](#-스크린샷)
- [문제 해결](#-문제-해결)

## 🚀 시작하기

### 사전 요구사항

- **Java 17** 이상
- **MySQL 8.0** 이상 (또는 다른 RDBMS)
- **Maven** 또는 **Gradle**

### 설치 및 실행

1. **레포지토리 클론**

```bash
git clone https://github.com/your-username/spring-security-jwt-thymeleaf.git
cd spring-security-jwt-thymeleaf
```

2. **데이터베이스 설정**

```sql
CREATE DATABASE security_login;
```

3. **application.properties 설정**

```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://localhost:3306/security_login?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT 설정 (512비트 이상 필수)
jwt.secret=myVerySecureAndLongSecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLong1234567890
jwt.expiration=86400000

# JPA 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **의존성 설치 및 애플리케이션 실행**

```bash
# Maven 사용 시
./mvnw spring-boot:run

# Gradle 사용 시
./gradlew bootRun
```

5. **브라우저에서 접속**

```
http://localhost:8080
```

## 🛠 기술 스택

### Backend

| 기술                | 버전 | 설명                    |
| ------------------- | ---- | ----------------------- |
| **Java**            | 17   | 메인 프로그래밍 언어    |
| **Spring Boot**     | 3.x  | 애플리케이션 프레임워크 |
| **Spring Security** | 6.x  | 인증 및 인가            |
| **Spring Data JPA** | 3.x  | 데이터 접근 계층        |
| **Thymeleaf**       | 3.x  | 서버사이드 템플릿 엔진  |

### Database & Security

| 기술           | 버전   | 설명              |
| -------------- | ------ | ----------------- |
| **MySQL**      | 8.0+   | 메인 데이터베이스 |
| **JWT (JJWT)** | 0.12.3 | JWT 토큰 처리     |
| **BCrypt**     | -      | 비밀번호 암호화   |

### Frontend

| 기술            | 버전  | 설명                       |
| --------------- | ----- | -------------------------- |
| **Bootstrap**   | 5.1.3 | CSS 프레임워크             |
| **FontAwesome** | 6.0.0 | 아이콘 라이브러리          |
| **JavaScript**  | ES6+  | 클라이언트 사이드 스크립트 |

## 📁 프로젝트 구조

```
src/main/java/com/example/SecurityLogin/
├── 📂 config/
│   ├── 🔧 JwtProperties.java          # JWT 설정값 관리
│   └── 🔧 SecurityConfig.java         # Spring Security 설정
├── 📂 controller/
│   ├── 🌐 AuthController.java         # REST API 인증 컨트롤러
│   ├── 🌐 WebAuthController.java      # 웹 폼 인증 컨트롤러
│   ├── 🌐 WebController.java          # 웹 페이지 컨트롤러
│   ├── 🌐 TestController.java         # API 테스트 컨트롤러
│   └── 🌐 GlobalControllerAdvice.java # 전역 컨트롤러 어드바이스
├── 📂 dto/
│   ├── 📋 JwtResponse.java           # JWT 응답 DTO
│   ├── 📋 LoginRequest.java          # 로그인 요청 DTO
│   └── 📋 SignupRequest.java         # 회원가입 요청 DTO
├── 📂 entity/
│   ├── 👤 Member.java                # 회원 엔티티 (UserDetails 구현)
│   └── 🏷️ Role.java                  # 권한 열거형 (USER, ADMIN)
├── 📂 repository/
│   └── 🗄️ MemberRepository.java      # 회원 레포지토리
├── 📂 security/
│   └── 🔒 JwtAuthenticationFilter.java # JWT 인증 필터
├── 📂 service/
│   ├── 🔧 CustomUserDetailsService.java # 사용자 상세 서비스
│   └── 🔧 MemberService.java         # 회원 관리 서비스
├── 📂 util/
│   └── 🛠️ JwtTokenUtil.java          # JWT 토큰 유틸리티
└── 🚀 SecurityLoginApplication.java  # 메인 애플리케이션

src/main/resources/
├── 📂 static/
│   ├── 📂 css/
│   ├── 📂 js/
│   └── 📂 images/
├── 📂 templates/
│   ├── 📂 auth/
│   │   ├── 🔐 login.html            # 로그인 페이지
│   │   └── 📝 signup.html           # 회원가입 페이지
│   ├── 📂 user/
│   │   └── 📊 dashboard.html        # 사용자 대시보드
│   ├── 📂 admin/
│   │   └── ⚙️ dashboard.html        # 관리자 대시보드
│   ├── 📂 error/
│   │   ├── 🚫 403.html             # 접근 거부 페이지
│   │   └── 🔍 404.html             # 페이지 없음
│   └── 🏠 index.html               # 메인 페이지
└── ⚙️ application.properties        # 애플리케이션 설정
```

## 📡 API 사용법

### 🔐 인증 API

#### 회원가입

```http
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "홍길동",
  "phoneNumber": "010-1234-5678"
}
```

**응답**

```json
"회원가입이 성공적으로 완료되었습니다!"
```

#### 로그인

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**응답**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjc5ODM2ODAwLCJleHAiOjE2Nzk5MjMyMDB9...",
  "type": "Bearer",
  "email": "user@example.com",
  "nickname": "홍길동"
}
```

### 🔒 인증이 필요한 API

#### 사용자 전용 API

```http
GET /api/user/test
Authorization: Bearer <JWT_TOKEN>
```

#### 관리자 전용 API

```http
GET /api/admin/stats
Authorization: Bearer <JWT_TOKEN>
```

**응답**

```json
{
  "totalUsers": 1234,
  "activeTokens": 856,
  "dailyLogins": 342,
  "securityAlerts": 3,
  "serverStatus": "HEALTHY"
}
```

## 🌐 웹 페이지 사용법

### 📱 페이지 구조

| URL                | 설명            | 접근 권한          |
| ------------------ | --------------- | ------------------ |
| `/`                | 메인 페이지     | 🌍 모든 사용자     |
| `/signup`          | 회원가입 페이지 | 🌍 비로그인 사용자 |
| `/login`           | 로그인 페이지   | 🌍 비로그인 사용자 |
| `/user/dashboard`  | 사용자 대시보드 | 👤 USER 이상       |
| `/admin/dashboard` | 관리자 대시보드 | 👨‍💼 ADMIN만         |

### 🎯 주요 기능

#### 💫 자동 권한 할당

- **첫 번째 회원가입**: 자동으로 `ADMIN` 권한 부여
- **이후 회원가입**: `USER` 권한 부여

#### 🔄 자동 리다이렉트

- **로그인 성공**: 권한에 따라 적절한 대시보드로 이동
  - `ADMIN` → `/admin/dashboard`
  - `USER` → `/user/dashboard`

#### 🍪 쿠키 기반 인증

- **HttpOnly 쿠키**: XSS 공격 방지
- **24시간 유효**: 자동 로그인 유지
- **보안 삭제**: 로그아웃 시 쿠키 완전 삭제

## ⚙️ 설정 방법

### 🔑 JWT 보안 설정

#### 프로덕션 환경

```properties
# 환경변수 사용 권장
jwt.secret=${JWT_SECRET:your-production-secret-key-here}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

```bash
# 환경변수 설정
export JWT_SECRET="your-very-long-and-secure-secret-key-for-production"
export JWT_EXPIRATION="86400000"
```

#### 안전한 시크릿 키 생성

```java
// 자동 생성 코드
SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
String encodedKey = Encoders.BASE64.encode(key.getEncoded());
System.out.println("Generated JWT Secret: " + encodedKey);
```

### 🗄️ 데이터베이스 설정

#### PostgreSQL 사용 시

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/security_login
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

#### H2 (테스트용)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
```

## 🌟 주요 기능

### 🎨 사용자 인터페이스

- **반응형 디자인**: 모바일부터 데스크톱까지
- **다크/라이트 모드**: 사용자 선호도 지원
- **애니메이션 효과**: 부드러운 전환 효과
- **접근성**: WCAG 2.1 가이드라인 준수

### 🔐 보안 기능

- **CSRF 보호**: API는 비활성화, 웹은 활성화
- **XSS 방지**: HttpOnly 쿠키 사용
- **SQL 인젝션 방지**: JPA 파라미터화 쿼리
- **브루트 포스 방지**: 로그인 시도 제한 (옵션)

### 📊 모니터링 & 로깅

- **상세 로깅**: 인증 과정 추적 가능
- **에러 처리**: 사용자 친화적 오류 메시지
- **디버깅 모드**: 개발 환경 전용 디버그 정보

## 📸 스크린샷

### 🏠 메인 페이지

```
┌─────────────────────────────────────┐
│  🛡️ Spring Security JWT            │
│                                    │
│  안전하고 확장 가능한 JWT 기반 인증   │
│                                    │
│  [ 시작하기 ]    [ 로그인 ]         │
└─────────────────────────────────────┘
```

### 🔐 로그인 페이지

```
┌─────────────────────────────────────┐
│           🔑 로그인                 │
│  ┌─────────────────────────────────┐│
│  │ 📧 이메일                       ││
│  │ [________________]              ││
│  │                                ││
│  │ 🔒 비밀번호                     ││
│  │ [________________]              ││
│  │                                ││
│  │      [ 로그인 ]                ││
│  └─────────────────────────────────┘│
│                                    │
│  계정이 없으신가요? 회원가입          │
└─────────────────────────────────────┘
```

### 📊 사용자 대시보드

```
┌─────────────────────────────────────┐
│  📊 사용자 대시보드                  │
│                                    │
│  ┌─────┐ ┌─────┐ ┌─────┐           │
│  │ 👤  │ │ 🛡️  │ │ ⏰  │           │
│  │프로필│ │보안 │ │세션 │           │
│  └─────┘ └─────┘ └─────┘           │
│                                    │
│  📋 계정 정보                       │
│  ┌─────────────────────────────────┐│
│  │ 이메일: user@example.com        ││
│  │ 권한: USER                     ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

## 🐛 문제 해결

### ❌ 자주 발생하는 오류들

#### 1. JWT 키 길이 오류

```
The signing key's size is 440 bits which is not secure enough for the HS512 algorithm
```

**해결방법**: JWT 시크릿 키를 64바이트(512비트) 이상으로 설정

```properties
jwt.secret=your-very-long-secret-key-that-is-at-least-64-characters-long-1234567890
```

#### 2. 403 Forbidden (웹 폼)

```
POST /signup 403 Forbidden
```

**해결방법**: SecurityConfig에서 해당 경로 허용

```java
.requestMatchers("/signup", "/auth/signup").permitAll()
```

#### 3. CORS 오류 (API 호출)

```
Access to fetch at 'http://localhost:8080/api/auth/login' from origin 'http://localhost:3000' has been blocked by CORS policy
```

**해결방법**: 컨트롤러에 CORS 설정 추가

```java
@CrossOrigin(origins = "http://localhost:3000")
@RestController
```

#### 4. 데이터베이스 연결 오류

```
Access denied for user 'root'@'localhost'
```

**해결방법**:

- MySQL 서비스 실행 확인
- 사용자명/비밀번호 확인
- 데이터베이스 존재 여부 확인

### 🔧 디버깅 팁

#### 로그 레벨 조정

```properties
# 상세 디버깅
logging.level.com.example.SecurityLogin=DEBUG
logging.level.org.springframework.security=DEBUG

# SQL 쿼리 확인
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

#### JWT 토큰 디코딩

```bash
# jwt.io 사이트에서 토큰 내용 확인
# 또는 온라인 JWT 디버거 사용
```

## 🚦 테스트 가이드

### 🧪 기본 테스트 시나리오

1. **회원가입 테스트**

   - 첫 번째 사용자 → ADMIN 권한 확인
   - 두 번째 사용자 → USER 권한 확인

2. **로그인 테스트**

   - 올바른 자격증명 → 대시보드 리다이렉트
   - 잘못된 자격증명 → 오류 메시지

3. **권한 테스트**

   - USER로 관리자 페이지 접근 → 403 오류
   - ADMIN으로 모든 페이지 접근 → 정상

4. **API 테스트**
   - JWT 토큰 없이 보호된 API 호출 → 401 오류
   - 유효한 토큰으로 API 호출 → 정상 응답

### 🔍 Postman 컬렉션

```json
{
  "info": { "name": "Spring Security JWT API" },
  "item": [
    {
      "name": "회원가입",
      "request": {
        "method": "POST",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "body": {
          "raw": "{\n  \"email\": \"test@example.com\",\n  \"password\": \"password123\",\n  \"nickname\": \"테스트사용자\",\n  \"phoneNumber\": \"010-1234-5678\"\n}"
        },
        "url": "{{baseUrl}}/api/auth/signup"
      }
    }
  ]
}
```

## 🔄 업그레이드 계획

### v2.0 예정 기능

- [ ] **소셜 로그인**: Google, GitHub OAuth2
- [ ] **이메일 인증**: 회원가입 시 이메일 확인
- [ ] **비밀번호 재설정**: 이메일을 통한 비밀번호 복구
- [ ] **프로필 관리**: 사용자 정보 수정
- [ ] **JWT 리프레시 토큰**: 자동 토큰 갱신
- [ ] **관리자 패널**: 사용자 관리, 권한 설정
- [ ] **API 문서화**: Swagger/OpenAPI 3.0
- [ ] **컨테이너화**: Docker & Docker Compose

### v3.0 장기 계획

- [ ] **마이크로서비스**: Spring Cloud 적용
- [ ] **실시간 기능**: WebSocket 지원
- [ ] **캐싱**: Redis 통합
- [ ] **모니터링**: Actuator + Micrometer
- [ ] **테스트**: 통합 테스트 완성
- [ ] **CI/CD**: GitHub Actions 파이프라인

## 🤝 기여하기

1. 프로젝트를 포크합니다
2. 기능 브랜치를 생성합니다 (`git checkout -b feature/amazing-feature`)
3. 변경사항을 커밋합니다 (`git commit -m 'Add some amazing feature'`)
4. 브랜치에 푸시합니다 (`git push origin feature/amazing-feature`)
5. Pull Request를 생성합니다

### 📋 기여 가이드라인

- **코드 스타일**: Google Java Style Guide 준수
- **커밋 메시지**: Conventional Commits 형식
- **테스트**: 새로운 기능에 대한 테스트 코드 포함
- **문서화**: README 및 코드 주석 업데이트

## 📞 연락처 & 지원

### 🛠️ 기술 지원

- **Issues**: [GitHub Issues](https://github.com/your-username/spring-security-jwt-thymeleaf/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-username/spring-security-jwt-thymeleaf/discussions)

### 👨‍💻 개발자 정보

- **GitHub**: [@Lee997a](https://github.com/Lee997a)
- **Email**: ehddn5476@gmail.com

### 📚 추가 자료

- **Spring Security 공식 문서**: [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)
- **JWT 공식 사이트**: [https://jwt.io/](https://jwt.io/)
- **Thymeleaf 가이드**: [https://www.thymeleaf.org/](https://www.thymeleaf.org/)

---

<div align="center">

**⭐ 이 프로젝트가 도움이 되었다면 스타를 눌러주세요! ⭐**

Made with ❤️ by [Your Name](https://github.com/your-username)

</div>
