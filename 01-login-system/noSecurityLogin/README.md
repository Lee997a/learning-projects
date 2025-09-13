# 🔐 Spring Boot 로그인/회원가입 시스템

Spring Boot와 JPA를 사용한 간단한 로그인 및 회원가입 시스템입니다.  
Spring Security 없이 기본적인 세션 기반 인증을 구현했습니다.

## 📋 프로젝트 개요

- **목적**: Spring Boot + JPA를 활용한 기본 로그인 시스템 구현
- **특징**: Spring Security 없이 순수 세션 기반 인증
- **개발환경**: Java 17, Spring Boot 3.x, JPA, H2 Database

## 🛠️ 기술 스택

- **Backend**: Spring Boot 3.5.5, Spring Data JPA
- **Database**: H2 Database (인메모리)
- **Template Engine**: Thymeleaf
- **Build Tool**: Gradle
- **Java Version**: 17

## 📁 프로젝트 구조

```
src/main/java/com/example/noSecurityLogin/
├── controller/
│   └── Loginontroller.java       # HTTP 요청 처리
├── dto/
│   └── MemberDto.java             # 데이터 전송 객체
├── entity/
│   └── Member.java                # JPA 엔티티
├── repository/
│   └── MemberRepository.java      # 데이터 접근 계층
└── service/
    ├── MemberService.java         # 서비스 인터페이스
    └── MemberServiceImpl.java     # 서비스 구현체

src/main/resources/
├── templates/
│   ├── home.html                  # 메인 페이지
│   ├── join.html                  # 회원가입 페이지
│   └── login.html                 # 로그인 페이지
└── application.properties                # 스프링 설정
```

## ✨ 주요 기능

### 🔹 회원가입

- 이메일 중복 체크
- 필수 정보 입력 (이메일, 비밀번호, 닉네임, 전화번호)
- 데이터베이스 저장

### 🔹 로그인

- 이메일과 비밀번호 검증
- 세션 기반 로그인 상태 관리
- 로그인 성공 시 메인 페이지 리다이렉트

### 🔹 로그아웃

- 세션 무효화
- 메인 페이지로 리다이렉트

## 🗄️ 데이터베이스 설계

### Member 테이블

| 컬럼명       | 타입    | 제약조건         | 설명     |
| ------------ | ------- | ---------------- | -------- |
| id           | BIGINT  | PK, AI           | 회원 ID  |
| email        | VARCHAR | NOT NULL, UNIQUE | 이메일   |
| password     | VARCHAR | NOT NULL         | 비밀번호 |
| nick_name    | VARCHAR | NOT NULL         | 닉네임   |
| phone_number | VARCHAR | NOT NULL         | 전화번호 |

## 🚀 실행 방법

### 1️⃣ 레포지토리 클론

```bash
git clone https://github.com/Lee997a/learning-projects.git
cd learning-projects
```

### 2️⃣ 의존성 설치 및 실행

```bash

# Gradle 사용시
./gradlew bootRun
```

### 3️⃣ 브라우저에서 확인

```
http://localhost:8080

```

## 📝 API 엔드포인트

| HTTP 메서드 | URL       | 설명            |
| ----------- | --------- | --------------- |
| GET         | `/`       | 메인 페이지     |
| GET         | `/join`   | 회원가입 페이지 |
| POST        | `/join`   | 회원가입 처리   |
| GET         | `/login`  | 로그인 페이지   |
| POST        | `/login`  | 로그인 처리     |
| POST        | `/logout` | 로그아웃 처리   |

## 🔧 설정 파일

### application.properties

```properties

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/testdb
spring.datasource.username=root
spring.datasource.password=1234!@#$

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 🔍 H2 데이터베이스 콘솔

개발 중 데이터베이스 상태를 확인하려면:

```
http://localhost:8080/h2-console
```

**접속 정보:**

- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
- Password: (비워두기)

## 📱 화면 구성

### 메인 페이지

- 로그인 전: 로그인/회원가입 링크 표시
- 로그인 후: 환영 메시지와 로그아웃 버튼 표시

### 회원가입 페이지

- 이메일, 비밀번호, 닉네임, 전화번호 입력 폼
- 이메일 중복시 에러 메시지 표시

### 로그인 페이지

- 이메일, 비밀번호 입력 폼
- 로그인 실패시 에러 메시지 표시

## ⚠️ 주의사항

### 보안 관련

- **비밀번호 암호화 미적용**: 현재 평문으로 저장됨 (실제 운영환경에서는 BCrypt 등 사용 필요)
- **HTTPS 미적용**: 로컬 개발용으로만 사용
- **입력값 검증 부족**: 실제 서비스에서는 더 엄격한 검증 필요

### 개발 환경 전용

- H2 인메모리 DB 사용 (서버 재시작시 데이터 초기화)
- 세션 기반 인증 (확장성 제한)

## 🚧 개선 예정 사항

- [ ] 비밀번호 암호화 (BCrypt)
- [ ] 입력값 유효성 검증 강화
- [ ] Spring Security 적용 버전 추가
- [ ] JWT 토큰 기반 인증 구현

## 👨‍💻 개발자

- **이름**: Lee997a
- **이메일**: ehddn5476@gmail.com
- **GitHub**: [Lee997a](https://github.com/Lee997a)

## 📞 문의

프로젝트에 대한 질문이나 제안사항이 있으시면 언제든 연락주세요!

---

⭐ 이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!
