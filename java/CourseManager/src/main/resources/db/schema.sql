-- Tabela użytkowników
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
                                          name VARCHAR(50) UNIQUE NOT NULL
);

-- Prowadzący
CREATE TABLE IF NOT EXISTS instructors (
                                           id BIGSERIAL PRIMARY KEY,
                                           first_name VARCHAR(50) NOT NULL,
                                           last_name VARCHAR(50) NOT NULL
);

-- Kursy
CREATE TABLE IF NOT EXISTS courses (
                                       id BIGSERIAL PRIMARY KEY,
                                       title VARCHAR(100) NOT NULL,
                                       description TEXT,
                                       start_date DATE NOT NULL,
                                       max_seats INT NOT NULL,
                                       category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
                                       instructor_id BIGINT REFERENCES instructors(id) ON DELETE SET NULL
);

-- Zapisy na kursy
CREATE TABLE IF NOT EXISTS enrollments (
                                           id BIGSERIAL PRIMARY KEY,
                                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                           course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
                                           status VARCHAR(20) DEFAULT 'AKTYWNY',
                                           enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           UNIQUE(user_id, course_id, status)
);

-- ============================================================
-- INDEKSY (optymalizacja zapytań)
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_enrollments_user ON enrollments(user_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_courses_start_date ON courses(start_date);
CREATE INDEX IF NOT EXISTS idx_courses_category ON courses(category_id);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);