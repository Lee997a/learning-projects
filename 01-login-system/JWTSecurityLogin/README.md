# Spring Security JWT Authentication 🔐

Spring Boot와 Spring Security를 활용한 JWT 기반 인증 시스템입니다.

<!--
## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시작하기](#-시작하기)
- [API 사용법](#-api-사용법)
- [프로젝트 구조](#-프로젝트-구조)
- [설정 방법](#-설정-방법)
- [보안 설정](#-보안-설정)
- [문제 해결](#-문제-해결) -->

## 🚀 프로젝트 소개

이 프로젝트는 Spring Security와 JWT(JSON Web Token)를 활용하여 안전하고 확장 가능한 인증 시스템을 구현한 예제입니다.
세션 기반이 아닌 토큰 기반 인증을 통해 RESTful API의 무상태성을 유지하며, 마이크로서비스 아키텍처에 적합한 구조로 설계되었습니다.

## ✨ 주요 기능

- **🔐 JWT 기반 인증**: 세션을 사용하지 않는 무상태 인증
- **👤 회원가입/로그인**: 이메일 기반 사용자 관리
- **🛡️ 권한 기반 접근 제어**: USER/ADMIN 역할 구분
- **🔒 비밀번호 암호화**: BCrypt를 사용한 안전한 비밀번호 저장
- **⏰ 토큰 만료 관리**: 자동 토큰 유효성 검증
- **🗄️ JPA Auditing**: 생성/수정 시간 자동 관리

## 🛠 기술 스택

### Backend

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security 6.x**
- **Spring Data JPA**
- **MySQL** (또는 원하는 DB)

### 라이브러리

- **JJWT 0.12.3** - JWT 토큰 처리
- **BCrypt** - 비밀번호 암호화
- **Lombok** - 보일러플레이트 코드 감소

## 🏁 시작하기

### 사전 요구사항

- Java 17 이상
- MySQL 8.0 이상 (또는 다른 RDBMS)
- Maven 또는 Gradle

### 설치 및 실행

1. **레포지토리 클론**

```bash
git clone https://github.com/Lee997a/spring-security-jwt.git
cd spring-security-jwt
```

2. **데이터베이스 설정**

```sql
CREATE DATABASE security_login;
```

3. **application.properties 설정**

```properties
# 데이터베이스 설정
spring.datasource.url=jdbc:mysql://localhost:3306/security_login
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT 설정
jwt.secret=myVerySecureAndLongSecretKeyForJWTTokenGenerationThatIsAtLeast512BitsLong1234567890
jwt.expiration=86400000

# JPA 설정
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

4. **애플리케이션 실행**

```bash
./mvnw spring-boot:run
# 또는
gradle bootRun
```

## 📡 API 사용법

### 회원가입

```http
POST /api/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "nickname": "사용자",
  "phoneNumber": "010-1234-5678"
}
```

**응답 예시**

```json
"회원가입이 성공적으로 완료되었습니다!"
```

### 로그인

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**응답 예시**

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "email": "user@example.com",
  "nickname": "사용자"
}
```

### 인증이 필요한 API 호출

```http
GET /api/user/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

### API 엔드포인트 정리

| 메서드 | 엔드포인트         | 설명            | 인증 필요  |
| ------ | ------------------ | --------------- | ---------- |
| POST   | `/api/auth/signup` | 회원가입        | ❌         |
| POST   | `/api/auth/login`  | 로그인          | ❌         |
| GET    | `/api/user/**`     | 사용자 전용 API | ✅ (USER)  |
| GET    | `/api/admin/**`    | 관리자 전용 API | ✅ (ADMIN) |

## 📁 프로젝트 구조

```
src/main/java/com/example/SecurityLogin/
├── config/
│   ├── JwtProperties.java          # JWT 설정값 관리
│   └── SecurityConfig.java         # Spring Security 설정
├── controller/
│   └── AuthController.java         # 인증 관련 API
├── dto/
│   ├── JwtResponse.java           # JWT 응답 DTO
│   ├── LoginRequest.java          # 로그인 요청 DTO
│   └── SignupRequest.java         # 회원가입 요청 DTO
├── entity/
│   ├── Member.java                # 회원 엔티티
│   └── Role.java                  # 권한 열거형
├── repository/
│   └── MemberRepository.java      # 회원 레포지토리
├── security/
│   └── JwtAuthenticationFilter.java # JWT 인증 필터
├── service/
│   ├── CustomUserDetailsService.java # 사용자 상세 서비스
│   └── MemberService.java         # 회원 관리 서비스
└── util/
    └── JwtTokenUtil.java          # JWT 토큰 유틸리티
```

## ⚙️ 설정 방법

### JWT 시크릿 키 설정

프로덕션 환경에서는 환경변수를 사용하세요:

```bash
export JWT_SECRET="your-very-long-secret-key-here"
```

```properties
jwt.secret=${JWT_SECRET:default-secret-key}
```

### 데이터베이스 설정

다른 데이터베이스를 사용하려면 `application.properties`를 수정하세요:

**PostgreSQL 예시**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/security_login
spring.datasource.username=postgres
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 🔒 보안 설정

### JWT 토큰 보안

- **HS512 알고리즘** 사용 (최소 512비트 키 필요)
- **토큰 만료시간**: 24시간 (설정 가능)
- **Bearer 토큰** 방식으로 헤더에 전달

### 비밀번호 보안

- **BCrypt** 해싱 알고리즘 사용
- **솔트** 자동 생성으로 레인보우 테이블 공격 방지

### 접근 제어

```java
// SecurityConfig.java에서 설정
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/api/auth/**").permitAll()
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .requestMatchers("/api/user/**").hasRole("USER")
    .anyRequest().authenticated()
)
```

## 🐛 문제 해결

### 자주 발생하는 오류들

**1. JWT 키 길이 오류**

```
The signing key's size is 440 bits which is not secure enough for the HS512 algorithm
```

**해결방법**: JWT 시크릿 키를 64바이트(512비트) 이상으로 설정하세요.

**2. CORS 오류**
프론트엔드에서 API 호출 시 CORS 오류가 발생하면:

```java
@CrossOrigin(origins = "http://localhost:3000") // React 개발 서버 주소
```

**3. 데이터베이스 연결 오류**

- 데이터베이스 서버가 실행 중인지 확인
- 사용자명/비밀번호가 올바른지 확인
- 데이터베이스가 존재하는지 확인

## 📞 연락처

- **GitHub**: [Lee997a](https://github.com/Lee997a)
- **Email**: ehddn5476@gmail.com

---

⭐ 이 프로젝트가 도움이 되었다면 스타를 눌러주세요!
