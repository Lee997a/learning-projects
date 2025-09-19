# 📝 Simple Board System

Spring Boot와 MyBatis를 사용한 간단한 게시판 시스템입니다.

## 🚀 기술 스택

### Backend

- **Java 17**
- **Spring Boot 3.x**
- **Spring Web MVC**
- **MyBatis**
- **MySQL 8.0**

### Frontend

- **Thymeleaf**
- **Bootstrap 5**
- **HTML5/CSS3**

### Build Tool

- **Gradle**

## 📋 주요 기능

- ✅ 게시글 목록 조회
- ✅ 게시글 상세 조회
- ✅ 게시글 작성
- ✅ 게시글 수정
- ✅ 게시글 삭제
- ✅ 반응형 웹 디자인

## 🛠️ 설치 및 실행

### 1. 프로젝트 클론

```bash
git clone https://github.com/Lee997a/board-system.git
cd board-system
```

### 2. 데이터베이스 설정

MySQL에서 데이터베이스와 테이블을 생성합니다.

```sql
-- 데이터베이스 생성
CREATE DATABASE board_db;
USE board_db;

-- 테이블 생성
CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. 환경 설정

`src/main/resources/application.properties` 파일을 수정합니다.

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/board_test
spring.datasource.username=root
spring.datasource.password=1234!@#$
```

### 4. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는

```bash
./gradlew build
java -jar build/libs/boardPrj-0.0.1-SNAPSHOT.jar
```

### 5. 브라우저에서 확인

```
http://localhost:8080
```

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/
│   │   └── com/example/boardPrj/
│   │       ├── BoardPrjApplication.java      # 메인 애플리케이션
│   │       ├── controller/
│   │       │   └── BoardController.java      # 웹 컨트롤러
│   │       ├── service/
│   │       │   └── BoardService.java         # 비즈니스 로직
│   │       ├── mapper/
│   │       │   └── BoardMapper.java          # MyBatis 매퍼 인터페이스
│   │       └── dto/
│   │           └── BoardDTO.java             # 데이터 전송 객체
│   └── resources/
│       ├── mapper/
│       │   └── BoardMapper.xml               # MyBatis SQL 매핑
│       ├── templates/
│       │   └── board/                        # Thymeleaf 템플릿
│       │       ├── list.html                 # 게시글 목록
│       │       ├── detail.html               # 게시글 상세
│       │       ├── write.html                # 게시글 작성
│       │       └── edit.html                 # 게시글 수정
│       └── application.yml                   # 애플리케이션 설정
```

## 🔧 주요 설정

### MyBatis 설정

```properties
mybatis.type-aliases-package=com.example.boardPrj
mybatis.mapper-locations=classpath:mapper/*.xml

mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```

### Thymeleaf 설정

## 📊 데이터베이스 스키마

### board 테이블

| 컬럼명     | 데이터 타입  | 제약 조건                   | 설명             |
| ---------- | ------------ | --------------------------- | ---------------- |
| id         | BIGINT       | PRIMARY KEY, AUTO_INCREMENT | 게시글 고유 번호 |
| title      | VARCHAR(255) | NOT NULL                    | 게시글 제목      |
| content    | TEXT         | NOT NULL                    | 게시글 내용      |
| writer     | VARCHAR(100) | NOT NULL                    | 작성자           |
| created_at | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP   | 작성일시         |

## 🌐 API 엔드포인트

| HTTP Method | URL                  | 설명               |
| ----------- | -------------------- | ------------------ |
| GET         | `/board`             | 게시글 목록 페이지 |
| GET         | `/board/{id}`        | 게시글 상세 페이지 |
| GET         | `/board/write`       | 게시글 작성 페이지 |
| POST        | `/board/write`       | 게시글 작성 처리   |
| GET         | `/board/{id}/edit`   | 게시글 수정 페이지 |
| POST        | `/board/{id}/edit`   | 게시글 수정 처리   |
| POST        | `/board/{id}/delete` | 게시글 삭제 처리   |

## 🎨 화면 구성

### 1. 게시글 목록

- 게시글 번호, 제목, 작성자, 작성일 표시
- 제목 클릭 시 상세 페이지로 이동
- 글쓰기 버튼

### 2. 게시글 상세

- 게시글 전체 내용 표시
- 수정/삭제 버튼
- 목록으로 돌아가기 버튼

### 3. 게시글 작성/수정

- 제목, 작성자, 내용 입력 폼
- 유효성 검사 적용

## 🔍 주요 특징

- **반응형 디자인**: Bootstrap을 사용하여 모바일 친화적
- **MyBatis 활용**: XML 기반 SQL 매핑으로 유연한 쿼리 관리
- **Thymeleaf 템플릿**: 서버사이드 렌더링으로 SEO 친화적
- **MVC 패턴**: 관심사 분리로 유지보수성 향상

## 🚧 향후 개발 계획

- [ ] 페이징 기능 추가
- [ ] 검색 기능 구현
- [ ] 파일 업로드 기능
- [ ] 댓글 시스템
- [ ] 사용자 인증 및 권한 관리
- [ ] 조회수 기능
- [ ] 게시글 추천/비추천

## 📞 문의

프로젝트에 대한 질문이나 제안사항이 있으시면 이슈를 생성해 주세요.

---

⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!
