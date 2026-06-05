INSERT INTO categories (name) VALUES ('Programowanie'), ('Bazy danych'), ('DevOps')
    ON CONFLICT (name) DO NOTHING;

INSERT INTO instructors (first_name, last_name) VALUES ('Jan', 'Kowalski'), ('Anna', 'Nowak')
    ON CONFLICT DO NOTHING;

-- hasło: admin (BCrypt: $2a$10$...)
INSERT INTO users (username, password, email, role) VALUES (
                                                               'admin',
                                                               '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiB6Q5jJ6Y4L4iP3r8X8vK1uG4W8k2K',
                                                               'admin@example.com',
                                                               'ROLE_ADMIN'
                                                           ) ON CONFLICT (username) DO NOTHING;