USE myschedule; -- 데이터베이스 선택

-- 담당자 테이블
CREATE TABLE Assignee
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,                                 -- 고유한 ID, 자동 증가
    name       VARCHAR(100) NOT NULL,                                             -- 이름 (필수)
    email      VARCHAR(100) NOT NULL UNIQUE,                                      -- 이메일 (필수, 고유)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,                                -- 생성 일시, 기본값 현재 시간
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,    -- 수정 일시, 기본값 현재 시간
    CONSTRAINT chk_email_format CHECK (email LIKE '%_@__%.__%'),                  -- 이메일 형식 체크
    INDEX idx_email (email)                                                       -- 이메일에 대한 인덱스
);

-- 일정 테이블
CREATE TABLE Task
(
    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,                 -- 고유한 ID, 자동 증가
    task_name   VARCHAR(200) NOT NULL CHECK (LENGTH(task_name) >= 3),             -- 일정 이름 (필수, 최소 길이 3자)
    assignee_id BIGINT,                                                           -- 담당자 ID (참조, 외래 키)
    password    VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8),              -- 비밀번호 (필수, 최소 길이 8자)
    created_at  DATETIME   DEFAULT CURRENT_TIMESTAMP,                             -- 생성 일시, 기본값 현재 시간
    updated_at  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정 일시, 기본값 현재 시간
    FOREIGN KEY (assignee_id) REFERENCES Assignee (id),                           -- 담당자 테이블과의 외래 키 관계
    INDEX idx_assignee_id (assignee_id)                                           -- 담당자 ID에 대한 인덱스
);

-- Assignee 테이블에 더미 데이터 삽입
INSERT INTO Assignee (name, email) VALUES ('홍길동', 'hong123@example.com');
INSERT INTO Assignee (name, email) VALUES ('김두한', 'kim443@example.com');
INSERT INTO Assignee (name, email) VALUES ('박춘식', 'park543@example.com');

-- Task 테이블에 더미 데이터 삽입
INSERT INTO Task (task_name, assignee_id, password, created_at, updated_at)
VALUES ('프로젝트 설계', 1, 'password123', '2024-08-15', '2024-08-15');

INSERT INTO Task (task_name, assignee_id, password, created_at, updated_at )
VALUES ('개발 진행', 2, 'password123', '2024-09-15', '2024-08-15');

INSERT INTO Task (task_name, assignee_id, password, created_at, updated_at )
VALUES ('코드 리뷰', 3, 'password123', '2024-010-15', '2024-08-15');
