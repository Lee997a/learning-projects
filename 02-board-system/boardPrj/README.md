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
- **Bootstrap Icons**
- **HTML5/CSS3/JavaScript**

### Build Tool
- **Gradle**

## 📋 주요 기능

- ✅ 게시글 목록 조회
- ✅ 게시글 상세 조회
- ✅ 게시글 작성
- ✅ 게시글 수정
- ✅ 게시글 삭제
- ✅ **좋아요 기능** (추가/취소)
- ✅ **실시간 좋아요 수 업데이트** (AJAX)
- ✅ **중복 좋아요 방지** (IP 기반)
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

-- board 테이블 생성
CREATE TABLE board (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    writer VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    like_count INT DEFAULT 0
);

-- board_like 테이블 생성 (좋아요 기능)
CREATE TABLE board_like (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    user_ip VARCHAR(45) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE,
    UNIQUE KEY unique_like (board_id, user_ip)
);
```

### 3. 환경 설정
`src/main/resources/application.yml` 파일을 수정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/board_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
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
│   │       │   ├── BoardService.java         # 게시글 비즈니스 로직
│   │       │   └── BoardLikeService.java     # 좋아요 비즈니스 로직
│   │       ├── mapper/
│   │       │   ├── BoardMapper.java          # 게시글 MyBatis 매퍼
│   │       │   └── BoardLikeMapper.java      # 좋아요 MyBatis 매퍼
│   │       └── dto/
│   │           ├── BoardDTO.java             # 게시글 데이터 전송 객체
│   │           └── BoardLikeDTO.java         # 좋아요 데이터 전송 객체
│   └── resources/
│       ├── mapper/
│       │   ├── BoardMapper.xml               # 게시글 SQL 매핑
│       │   └── BoardLikeMapper.xml           # 좋아요 SQL 매핑
│       ├── templates/
│       │   └── board/                        # Thymeleaf 템플릿
│       │       ├── list.html                 # 게시글 목록
│       │       ├── detail.html               # 게시글 상세 (좋아요 기능 포함)
│       │       ├── write.html                # 게시글 작성
│       │       └── edit.html                 # 게시글 수정
│       └── application.yml                   # 애플리케이션 설정
```

## 🔧 주요 설정

### MyBatis 설정
```properties
mybatis.type-aliases-package=com.example.boardPrj.dto
mybatis.mapper-locations=classpath:mapper/*.xml
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```


## 📊 데이터베이스 스키마

### board 테이블
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 게시글 고유 번호 |
| title | VARCHAR(255) | NOT NULL | 게시글 제목 |
| content | TEXT | NOT NULL | 게시글 내용 |
| writer | VARCHAR(100) | NOT NULL | 작성자 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 작성일시 |
| like_count | INT | DEFAULT 0 | 좋아요 수 |

### board_like 테이블 (좋아요 기능)
| 컬럼명 | 데이터 타입 | 제약 조건 | 설명 |
|--------|-------------|-----------|------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 좋아요 고유 번호 |
| board_id | BIGINT | NOT NULL, FOREIGN KEY | 게시글 ID |
| user_ip | VARCHAR(45) | NOT NULL | 사용자 IP |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | 좋아요 날짜 |
| | | UNIQUE(board_id, user_ip) | 중복 좋아요 방지 |

## 🌐 API 엔드포인트

| HTTP Method | URL | 설명 |
|-------------|-----|------|
| GET | `/board` | 게시글 목록 페이지 |
| GET | `/board/{id}` | 게시글 상세 페이지 |
| GET | `/board/write` | 게시글 작성 페이지 |
| POST | `/board/write` | 게시글 작성 처리 |
| GET | `/board/{id}/edit` | 게시글 수정 페이지 |
| POST | `/board/{id}/edit` | 게시글 수정 처리 |
| POST | `/board/{id}/delete` | 게시글 삭제 처리 |
| **POST** | **`/board/{id}/like`** | **좋아요 토글 (AJAX)** |

## 🎨 화면 구성

### 1. 게시글 목록
- 게시글 번호, 제목, 작성자, **좋아요 수**, 작성일 표시
- 제목 클릭 시 상세 페이지로 이동
- 글쓰기 버튼
- **좋아요 수가 있는 게시글은 하트 아이콘과 함께 표시**

### 2. 게시글 상세
- 게시글 전체 내용 표시
- **실시간 좋아요 버튼** (클릭 시 AJAX로 즉시 반영)
- **좋아요 상태에 따른 하트 아이콘 변화** (빈 하트 ↔ 채워진 하트)
- 수정/삭제 버튼
- 목록으로 돌아가기 버튼

### 3. 게시글 작성/수정
- 제목, 작성자, 내용 입력 폼
- 유효성 검사 적용

## ❤️ 좋아요 기능 상세

### 주요 특징
- **실시간 업데이트**: 페이지 새로고침 없이 AJAX로 즉시 반영
- **중복 방지**: IP 주소 기반으로 한 사용자당 한 번만 좋아요 가능
- **시각적 피드백**: 좋아요 상태에 따라 하트 아이콘 및 버튼 색상 변경
- **트랜잭션 처리**: 데이터 일관성 보장
- **에러 처리**: 네트워크 오류 시 사용자에게 알림

### 좋아요 동작 과정
1. 사용자가 하트 버튼 클릭
2. 현재 좋아요 상태 확인 (좋아요/좋아요 안함)
3. 상태에 따라 좋아요 추가 또는 취소
4. board_like 테이블에 데이터 추가/삭제
5. board 테이블의 like_count 컬럼 업데이트
6. 프론트엔드에서 UI 즉시 업데이트

### 데이터베이스 관계
```
board (1) ←→ (N) board_like
- 한 게시글에 여러 좋아요 가능
- 한 IP당 게시글마다 하나의 좋아요만 가능
- 게시글 삭제 시 관련 좋아요도 자동 삭제 (CASCADE)
```

## 🔍 주요 특징

- **반응형 디자인**: Bootstrap을 사용하여 모바일 친화적
- **MyBatis 활용**: XML 기반 SQL 매핑으로 유연한 쿼리 관리
- **Thymeleaf 템플릿**: 서버사이드 렌더링으로 SEO 친화적
- **MVC 패턴**: 관심사 분리로 유지보수성 향상
- **AJAX 통신**: 좋아요 기능에서 비동기 처리로 사용자 경험 개선
- **트랜잭션 관리**: Spring의 `@Transactional`로 데이터 일관성 보장
- **IP 기반 중복 방지**: 동일 IP에서 중복 좋아요 방지

## 🛠️ 기술적 구현 세부사항

### 좋아요 기능 구현
```java
// Service Layer - 트랜잭션 처리
@Transactional
public boolean toggleLike(Long boardId, String userIp) {
    boolean isLiked = isLikedByUser(boardId, userIp);
    
    if (isLiked) {
        boardLikeMapper.delete(boardId, userIp);
    } else {
        boardLikeMapper.insert(new BoardLikeDTO(boardId, userIp));
    }
    
    updateLikeCount(boardId);
    return !isLiked;
}
```

### AJAX 통신
```javascript
// Frontend - 비동기 좋아요 처리
fetch(`/board/${boardId}/like`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' }
})
.then(response => response.json())
.then(data => {
    // UI 즉시 업데이트
    updateHeartIcon(data.isLiked);
    updateLikeCount(data.likeCount);
});
```

## 🚧 향후 개발 계획

- [ ] 페이징 기능 추가
- [ ] 검색 기능 구현
- [ ] 파일 업로드 기능
- [ ] 댓글 시스템
- [ ] 사용자 인증 및 권한 관리 (현재는 IP 기반)
- [ ] 조회수 기능
- [ ] 게시글 카테고리 분류
- [ ] **좋아요 많은 순 정렬 기능**
- [ ] **좋아요 애니메이션 효과**
- [ ] **실시간 알림 시스템**

## 🔧 추가 개선 가능한 기능

### 좋아요 관련 고도화
- **사용자 인증 시스템**: IP 대신 실제 사용자 ID 기반 좋아요
- **좋아요 통계**: 일별/월별 좋아요 추이 분석
- **인기 게시글**: 좋아요 기반 트렌딩 게시글
- **좋아요 알림**: 내 게시글에 좋아요 받을 때 알림

## 📞 문의

프로젝트에 대한 질문이나 제안사항이 있으시면 이슈를 생성해 주세요.

---
⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!