CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
    );

CREATE TABLE IF NOT EXISTS categories (
                                          id BIGSERIAL PRIMARY KEY,
                                          name VARCHAR(50) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS instructors (
                                           id BIGSERIAL PRIMARY KEY,
                                           first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS courses (
                                       id BIGSERIAL PRIMARY KEY,
                                       title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    max_seats INT NOT NULL,
    category_id BIGINT REFERENCES categories(id),
    instructor_id BIGINT REFERENCES instructors(id)
    );

CREATE TABLE IF NOT EXISTS enrollments (
                                           id BIGSERIAL PRIMARY KEY,
                                           user_id BIGINT REFERENCES users(id),
    course_id BIGINT REFERENCES courses(id),
    status VARCHAR(20) DEFAULT 'AKTYWNY',
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, course_id, status)
    );