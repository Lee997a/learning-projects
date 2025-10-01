# TripMate 프로젝트 초기 설정 가이드

## 1️⃣ 프로젝트 생성

### Frontend 설정
```bash
# Next.js 프로젝트 생성 (TypeScript, Tailwind 포함)
npx create-next-app@latest tripmate-frontend
# ✔ Would you like to use TypeScript? Yes
# ✔ Would you like to use ESLint? Yes
# ✔ Would you like to use Tailwind CSS? Yes
# ✔ Would you like to use `src/` directory? Yes
# ✔ Would you like to use App Router? Yes
# ✔ Would you like to customize the default import alias? No

cd tripmate-frontend
```

### Backend 설정
```bash
# 별도 폴더로 백엔드 생성
mkdir tripmate-backend
cd tripmate-backend
npm init -y

# 필수 패키지 설치
npm install express cors dotenv
npm install prisma @prisma/client
npm install bcrypt jsonwebtoken
npm install express-validator

# 개발 의존성
npm install --save-dev typescript @types/node @types/express
npm install --save-dev ts-node nodemon @types/bcrypt @types/jsonwebtoken
```

---

## 2️⃣ 폴더 구조

### Frontend 구조
```
tripmate-frontend/
├── src/
│   ├── app/                    # Next.js App Router
│   │   ├── (auth)/            # 인증 관련 페이지 그룹
│   │   │   ├── login/
│   │   │   └── register/
│   │   ├── (main)/            # 메인 앱 페이지 그룹
│   │   │   ├── dashboard/     # 대시보드 (홈)
│   │   │   ├── trips/         # 여행 목록/상세
│   │   │   ├── budget/        # 경비 관리
│   │   │   ├── records/       # 여행 기록
│   │   │   └── community/     # 커뮤니티
│   │   ├── layout.tsx
│   │   └── page.tsx
│   ├── components/            # 재사용 컴포넌트
│   │   ├── ui/               # 기본 UI 컴포넌트
│   │   │   ├── Button.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── Card.tsx
│   │   │   └── Modal.tsx
│   │   ├── features/         # 기능별 컴포넌트
│   │   │   ├── trip/
│   │   │   ├── budget/
│   │   │   ├── schedule/
│   │   │   └── record/
│   │   └── layout/           # 레이아웃 컴포넌트
│   │       ├── Header.tsx
│   │       ├── Sidebar.tsx
│   │       └── Footer.tsx
│   ├── lib/                  # 유틸리티, 헬퍼
│   │   ├── api.ts           # API 클라이언트
│   │   ├── utils.ts         # 공통 유틸
│   │   └── constants.ts     # 상수
│   ├── hooks/               # 커스텀 훅
│   │   ├── useAuth.ts
│   │   ├── useTrip.ts
│   │   └── useBudget.ts
│   ├── store/               # 상태 관리 (Zustand)
│   │   ├── authStore.ts
│   │   ├── tripStore.ts
│   │   └── uiStore.ts
│   ├── types/               # TypeScript 타입 정의
│   │   ├── trip.ts
│   │   ├── user.ts
│   │   └── budget.ts
│   └── styles/
│       └── globals.css
├── public/
│   ├── images/
│   └── icons/
├── .env.local
├── next.config.js
├── tailwind.config.js
└── package.json
```

**구조 설명:**
- **(auth), (main)**: 라우트 그룹으로 레이아웃 공유하지만 URL에는 영향 없음
- **components/ui**: 버튼, 인풋 같은 기본 컴포넌트 (재사용성 최고)
- **components/features**: 도메인별 특화 컴포넌트
- **lib**: 비즈니스 로직 없는 순수 함수들
- **hooks**: 컴포넌트 로직 분리로 재사용성 향상
- **store**: 전역 상태는 여기서만 관리
- **types**: 타입 중앙 관리로 일관성 유지

### Backend 구조
```
tripmate-backend/
├── src/
│   ├── controllers/          # 라우트 핸들러
│   │   ├── authController.ts
│   │   ├── tripController.ts
│   │   ├── budgetController.ts
│   │   ├── scheduleController.ts
│   │   ├── recordController.ts
│   │   └── communityController.ts
│   ├── services/            # 비즈니스 로직
│   │   ├── authService.ts
│   │   ├── tripService.ts
│   │   ├── aiService.ts    # AI 추천 로직
│   │   └── notificationService.ts
│   ├── routes/              # API 라우트
│   │   ├── auth.ts
│   │   ├── trips.ts
│   │   ├── budgets.ts
│   │   └── community.ts
│   ├── middlewares/         # 미들웨어
│   │   ├── auth.ts         # JWT 검증
│   │   ├── validate.ts     # 입력 검증
│   │   └── errorHandler.ts # 에러 핸들링
│   ├── utils/               # 유틸리티
│   │   ├── jwt.ts
│   │   ├── bcrypt.ts
│   │   └── upload.ts       # 파일 업로드
│   ├── types/               # TypeScript 타입
│   │   └── express.d.ts    # Express 타입 확장
│   ├── config/              # 설정
│   │   ├── database.ts
│   │   └── app.ts
│   └── app.ts              # Express 앱 설정
├── prisma/
│   ├── schema.prisma       # DB 스키마
│   └── migrations/         # DB 마이그레이션
├── .env
├── tsconfig.json
└── package.json
```

**구조 설명:**
- **Controller**: HTTP 요청/응답만 처리 (얇게 유지)
- **Service**: 실제 비즈니스 로직 (테스트하기 쉬움)
- **라우트와 컨트롤러 분리**: 라우팅 로직과 핸들러 분리로 가독성 향상
- **Middleware**: 인증, 검증 등 공통 로직 재사용
- **Prisma**: ORM으로 타입 안전한 DB 작업

---

## 3️⃣ 데이터베이스 스키마 설계

### Prisma Schema (prisma/schema.prisma)

```prisma
// Prisma 설정
generator client {
  provider = "prisma-client-js"
}

datasource db {
  provider = "postgresql"
  url      = env("DATABASE_URL")
}

// 사용자
model User {
  id            String    @id @default(cuid())
  email         String    @unique
  password      String
  name          String
  profileImage  String?
  bio           String?
  createdAt     DateTime  @default(now())
  updatedAt     DateTime  @updatedAt
  
  // 관계
  trips         TripMember[]
  expenses      Expense[]
  records       Record[]
  comments      Comment[]
  likes         Like[]
  posts         Post[]
  
  @@map("users")
}

// 여행
model Trip {
  id            String      @id @default(cuid())
  title         String
  destination   String
  startDate     DateTime
  endDate       DateTime
  coverImage    String?
  description   String?
  budget        Float?
  isPublic      Boolean     @default(false)
  createdAt     DateTime    @default(now())
  updatedAt     DateTime    @updatedAt
  
  // 관계
  members       TripMember[]
  schedules     Schedule[]
  expenses      Expense[]
  records       Record[]
  
  @@map("trips")
}

// 여행 참여자 (다대다 중간 테이블)
model TripMember {
  id        String   @id @default(cuid())
  role      String   @default("member") // owner, admin, member
  joinedAt  DateTime @default(now())
  
  userId    String
  user      User     @relation(fields: [userId], references: [id], onDelete: Cascade)
  
  tripId    String
  trip      Trip     @relation(fields: [tripId], references: [id], onDelete: Cascade)
  
  @@unique([userId, tripId])
  @@map("trip_members")
}

// 일정
model Schedule {
  id          String    @id @default(cuid())
  date        DateTime
  time        String?
  title       String
  location    String?
  latitude    Float?
  longitude   Float?
  description String?
  category    String?   // 숙박, 식사, 관광, 교통 등
  order       Int       @default(0)
  createdAt   DateTime  @default(now())
  updatedAt   DateTime  @updatedAt
  
  tripId      String
  trip        Trip      @relation(fields: [tripId], references: [id], onDelete: Cascade)
  
  @@map("schedules")
}

// 지출
model Expense {
  id          String    @id @default(cuid())
  amount      Float
  currency    String    @default("KRW")
  category    String    // 숙박, 식비, 교통, 액티비티 등
  description String
  date        DateTime
  receipt     String?   // 영수증 이미지 URL
  createdAt   DateTime  @default(now())
  updatedAt   DateTime  @updatedAt
  
  tripId      String
  trip        Trip      @relation(fields: [tripId], references: [id], onDelete: Cascade)
  
  paidBy      String
  payer       User      @relation(fields: [paidBy], references: [id])
  
  @@map("expenses")
}

// 여행 기록 (SNS 포스트)
model Record {
  id          String    @id @default(cuid())
  title       String
  content     String
  images      String[]  // 이미지 URL 배열
  location    String?
  latitude    Float?
  longitude   Float?
  isPublic    Boolean   @default(false)
  createdAt   DateTime  @default(now())
  updatedAt   DateTime  @updatedAt
  
  tripId      String
  trip        Trip      @relation(fields: [tripId], references: [id], onDelete: Cascade)
  
  authorId    String
  author      User      @relation(fields: [authorId], references: [id], onDelete: Cascade)
  
  comments    Comment[]
  likes       Like[]
  
  @@map("records")
}

// 댓글
model Comment {
  id          String    @id @default(cuid())
  content     String
  createdAt   DateTime  @default(now())
  updatedAt   DateTime  @updatedAt
  
  recordId    String
  record      Record    @relation(fields: [recordId], references: [id], onDelete: Cascade)
  
  authorId    String
  author      User      @relation(fields: [authorId], references: [id], onDelete: Cascade)
  
  @@map("comments")
}

// 좋아요
model Like {
  id          String    @id @default(cuid())
  createdAt   DateTime  @default(now())
  
  recordId    String
  record      Record    @relation(fields: [recordId], references: [id], onDelete: Cascade)
  
  userId      String
  user        User      @relation(fields: [userId], references: [id], onDelete: Cascade)
  
  @@unique([userId, recordId])
  @@map("likes")
}

// 커뮤니티 게시글
model Post {
  id          String    @id @default(cuid())
  title       String
  content     String
  tags        String[]
  viewCount   Int       @default(0)
  likeCount   Int       @default(0)
  isPublic    Boolean   @default(true)
  createdAt   DateTime  @default(now())
  updatedAt   DateTime  @updatedAt
  
  authorId    String
  author      User      @relation(fields: [authorId], references: [id], onDelete: Cascade)
  
  @@map("posts")
}
```

**스키마 설계 포인트:**
1. **CUID 사용**: UUID보다 짧고 정렬 가능
2. **Cascade 삭제**: 여행 삭제시 관련 데이터 자동 삭제
3. **인덱스**: 자주 조회되는 필드에 자동 인덱스 (unique, relation)
4. **타임스탬프**: 모든 테이블에 createdAt, updatedAt
5. **유연한 구조**: 배열 타입(images, tags)으로 확장성 확보

---

## 4️⃣ 환경 변수 설정

### Frontend (.env.local)
```env
# API
NEXT_PUBLIC_API_URL=http://localhost:5000/api

# Kakao Map
NEXT_PUBLIC_KAKAO_MAP_KEY=your_kakao_map_key

# Google OAuth (선택)
NEXT_PUBLIC_GOOGLE_CLIENT_ID=your_google_client_id

# 환경
NEXT_PUBLIC_ENV=development
```

### Backend (.env)
```env
# 서버
PORT=5000
NODE_ENV=development

# 데이터베이스
DATABASE_URL="postgresql://user:password@localhost:5432/tripmate?schema=public"

# JWT
JWT_SECRET=your-super-secret-jwt-key-change-this-in-production
JWT_EXPIRES_IN=7d

# AWS S3 (이미지 업로드)
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
AWS_REGION=ap-northeast-2
AWS_S3_BUCKET=tripmate-images

# Redis (선택, 캐싱용)
REDIS_URL=redis://localhost:6379

# 외부 API
OPENWEATHER_API_KEY=your_openweather_key
TOUR_API_KEY=your_tour_api_key
```

**환경 변수 관리 팁:**
- `.env.example` 파일 만들어서 Git에 커밋 (실제 값은 제외)
- 프로덕션 환경에서는 환경변수 서비스 사용 (Vercel, AWS Secrets Manager)
- 절대 Git에 실제 키 커밋하지 않기!

---

## 5️⃣ Git 전략

### Gitignore
```
# 프론트엔드
node_modules/
.next/
.env.local
.env.*.local

# 백엔드
node_modules/
dist/
.env
uploads/

# 공통
.DS_Store
*.log
```

### 브랜치 전략 (Git Flow 간소화 버전)
```
main (프로덕션)
  ↑
develop (개발)
  ↑
feature/기능명 (새 기능)
```

### 커밋 메시지 규칙
```
feat: 새로운 기능
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅
refactor: 리팩토링
test: 테스트 추가
chore: 빌드, 설정 변경

예시:
feat: 여행 생성 API 구현
fix: 경비 계산 오류 수정
docs: README 업데이트
```

---
