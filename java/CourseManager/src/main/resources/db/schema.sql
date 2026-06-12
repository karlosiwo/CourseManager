-- Tabela użytkowników aplikacji
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
);

-- Kategorie kursów
CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL CHECK (char_length(name) BETWEEN 3 AND 50)
);

-- Prowadzący
CREATE TABLE IF NOT EXISTS instructors (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL CHECK (char_length(first_name) >= 2),
    last_name VARCHAR(50) NOT NULL CHECK (char_length(last_name) >= 2),
    UNIQUE(first_name, last_name)
);

-- Kursy
CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL CHECK (char_length(title) >= 1),
    description TEXT CHECK (description IS NULL OR char_length(description) <= 2000),
    start_date DATE NOT NULL,
    max_seats INT NOT NULL CHECK (max_seats BETWEEN 1 AND 200),
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    instructor_id BIGINT REFERENCES instructors(id) ON DELETE SET NULL
);

-- Zapisy na kursy
CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    status VARCHAR(20) DEFAULT 'AKTYWNY' CHECK (status IN ('AKTYWNY', 'ANULOWANY')),
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, course_id, status)
);

-- Udostępnianie kursów konkretnym użytkownikom lub publicznym tokenem
CREATE TABLE IF NOT EXISTS course_shares (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    owner_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    token VARCHAR(80) UNIQUE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- INDEKSY (optymalizacja zapytań)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_enrollments_user ON enrollments(user_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_status ON enrollments(status);
CREATE INDEX IF NOT EXISTS idx_courses_start_date ON courses(start_date);
CREATE INDEX IF NOT EXISTS idx_courses_category ON courses(category_id);
CREATE INDEX IF NOT EXISTS idx_courses_title ON courses(title);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_course_shares_owner ON course_shares(owner_user_id);
CREATE INDEX IF NOT EXISTS idx_course_shares_target ON course_shares(target_user_id);
CREATE INDEX IF NOT EXISTS idx_course_shares_token ON course_shares(token);
