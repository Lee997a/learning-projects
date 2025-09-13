# 🔐 Spring Security 로그인/회원가입 시스템

Spring Boot와 Spring Security를 활용한 세션 기반 로그인 및 회원가입 시스템입니다.  
BCrypt 비밀번호 암호화와 JPA Auditing을 적용하여 보안성과 데이터 추적성을 강화했습니다.

## 📋 프로젝트 개요

- **목적**: Spring Security를 사용한 완전한 인증/인가 시스템 구현
- **특징**: 세션 기반 인증, 비밀번호 암호화, 회원가입 날짜 자동 추적
- **개발환경**: Java 17, Spring Boot 3.x, Spring Security 6.x, JPA, H2 Database

## ✨ 주요 기능

### 🔹 회원가입

- 이메일 중복 체크
- Bean Validation을 통한 입력값 검증
- BCrypt를 이용한 비밀번호 암호화
- JPA Auditing을 통한 가입일 자동 기록

### 🔹 로그인/로그아웃

- Spring Security 기반 인증
- 세션 기반 로그인 상태 관리
- 커스텀 로그인 페이지
- 로그인 실패시 에러 메시지 표시

### 🔹 마이페이지

- 로그인된 사용자 정보 표시
- 가입일 및 마지막 수정일 확인
- 권한 정보 표시

### 🔹 보안 기능

- CSRF 보호 (개발 환경에서는 비활성화)
- 비밀번호 암호화 (BCrypt)
- 세션 기반 인증
- 권한별 접근 제어

## 🛠️ 기술 스택

- **Backend**: Spring Boot 3.x, Spring Security 6.x, Spring Data JPA
- **Database**: H2 Database (인메모리)
- **Template Engine**: Thymeleaf (Spring Security 통합)
- **Validation**: Bean Validation
- **Password Encryption**: BCrypt
- **Build Tool**: Gradle
- **Java Version**: 17

## 📁 프로젝트 구조

```
src/main/java/com/example/securityLogin/
├── config/
│   ├── JpaAuditingConfig.java         # JPA Auditing 설정
│   └── SecurityConfig.java           # Spring Security 설정
├── controller/
│   └── MemberController.java         # HTTP 요청 처리
├── dto/
│   └── MemberJoinDto.java            # 회원가입 데이터 전송 객체
├── entity/
│   ├── Member.java                   # 회원 엔티티 (UserDetails 구현)
│   └── Role.java                     # 권한 열거형
├── repository/
│   └── MemberRepository.java         # 데이터 접근 계층
└── service/
    ├── CustomUserDetailsService.java # Spring Security 사용자 인증
    ├── MemberService.java            # 서비스 인터페이스
    └── MemberServiceImpl.java        # 서비스 구현체

src/main/resources/
├── templates/
│   ├── home.html                     # 메인 페이지
│   ├── join.html                     # 회원가입 페이지
│   ├── login.html                    # 로그인 페이지
│   └── mypage.html                   # 마이페이지
└── application.yml                   # 스프링 설정
```

## 🗄️ 데이터베이스 설계

### Member 테이블

| 컬럼명             | 타입      | 제약조건         | 설명               |
| ------------------ | --------- | ---------------- | ------------------ |
| id                 | BIGINT    | PK, AI           | 회원 ID            |
| email              | VARCHAR   | NOT NULL, UNIQUE | 이메일 (로그인 ID) |
| password           | VARCHAR   | NOT NULL         | 암호화된 비밀번호  |
| nick_name          | VARCHAR   | NOT NULL         | 닉네임             |
| phone_number       | VARCHAR   | NOT NULL         | 전화번호           |
| role               | VARCHAR   | NOT NULL         | 권한 (USER, ADMIN) |
| created_date       | TIMESTAMP | NOT NULL         | 가입일             |
| last_modified_date | TIMESTAMP |                  | 마지막 수정일      |

## 🚀 실행 방법

### 1️⃣ 요구사항

- Java 17 이상
- Gradle 7.0 이상

### 2️⃣ 레포지토리 클론

```bash
git clone https://github.com/your-username/spring-security-login.git
cd spring-security-login
```

### 3️⃣ 실행

```bash
# Gradle을 이용한 실행
./gradlew bootRun

# 또는 빌드 후 실행
./gradlew build
java -jar build/libs/security-login-0.0.1-SNAPSHOT.jar
```

### 4️⃣ 브라우저에서 확인

```
http://localhost:8080
```

## 📝 API 엔드포인트

| HTTP 메서드 | URL       | 설명                          | 인증 필요 |
| ----------- | --------- | ----------------------------- | --------- |
| GET         | `/`       | 메인 페이지                   | ❌        |
| GET         | `/join`   | 회원가입 페이지               | ❌        |
| POST        | `/join`   | 회원가입 처리                 | ❌        |
| GET         | `/login`  | 로그인 페이지                 | ❌        |
| POST        | `/login`  | 로그인 처리 (Spring Security) | ❌        |
| POST        | `/logout` | 로그아웃 처리                 | ✅        |
| GET         | `/mypage` | 마이페이지                    | ✅        |

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

## 🛡️ 보안 기능

### 비밀번호 암호화

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### 권한 기반 접근 제어

```java
http.authorizeHttpRequests(auth -> auth
    .requestMatchers("/", "/join", "/login").permitAll()
    .anyRequest().authenticated()
)
```

### UserDetails 구현

```java
@Entity
public class Member implements UserDetails {
    // Spring Security가 요구하는 사용자 정보 제공
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
```

## 📱 화면 구성

### 🏠 메인 페이지

- **비로그인**: 로그인/회원가입 링크
- **로그인 후**: 환영 메시지, 사용자 정보, 마이페이지/로그아웃 버튼

### 📝 회원가입 페이지

- 이메일, 비밀번호, 닉네임, 전화번호 입력 폼
- Bean Validation 에러 메시지 표시
- 이메일 중복시 에러 메시지

### 🔑 로그인 페이지

- 이메일, 비밀번호 입력 폼
- 로그인 실패시 에러 메시지
- 회원가입 완료시 성공 메시지

### 👤 마이페이지

- 사용자 정보 테이블 형태로 표시
- 가입일, 마지막 수정일 포함
- 권한 정보 표시

## ⚡ 주요 기능 동작 과정

### 회원가입 프로세스

1. 사용자가 회원가입 폼 제출
2. Bean Validation으로 입력값 검증
3. 이메일 중복 체크
4. 비밀번호 BCrypt 암호화
5. JPA Auditing으로 가입일 자동 설정
6. 데이터베이스 저장
7. 로그인 페이지로 리다이렉트

### 로그인 프로세스

1. Spring Security가 로그인 요청 처리
2. CustomUserDetailsService에서 사용자 조회
3. 비밀번호 검증 (BCrypt)
4. 인증 성공시 SecurityContext에 저장
5. 세션 생성 및 메인 페이지 리다이렉트

## 🔄 JPA Auditing

회원가입 날짜 자동 관리:

```java
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
```

## ⚠️ 주의사항

### 현재 설정 (개발용)

- **CSRF 비활성화**: 개발 편의를 위해 비활성화
- **H2 인메모리 DB**: 서버 재시작시 데이터 초기화
- **모든 요청 허용**: 현재 Security 설정이 간소화됨

### 운영환경 적용시 고려사항

- CSRF 보호 활성화
- HTTPS 적용
- MySQL/PostgreSQL 등 실제 DB 사용
- 더 엄격한 권한 설정
- 로그 레벨 조정

## 🚧 향후 개선 계획

- [ ] JWT 토큰 기반 인증 추가
- [ ] OAuth2 소셜 로그인 (Google, GitHub)
- [ ] 이메일 인증 기능
- [ ] 비밀번호 찾기/변경 기능
- [ ] 관리자 페이지 구현
- [ ] API 응답 형태로 REST API 제공
- [ ] Docker 컨테이너화

## 👨‍💻 개발자 정보

- **이름**: [Lee997a]
- **이메일**: [ehddn5476@gmail.com]
- **GitHub**: [@Lee997a](https://github.com/Lee997a)

## 🙏 참고 자료

- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)

---

⭐ 이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!

## 📞 문의

프로젝트에 대한 질문이나 제안사항이 있으시면 언제든 연락주세요!
