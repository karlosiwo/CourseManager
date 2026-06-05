-- Kategorie
INSERT INTO categories (name) VALUES ('Programowanie'), ('Bazy danych'), ('DevOps')
ON CONFLICT (name) DO NOTHING;

-- Prowadzący
INSERT INTO instructors (first_name, last_name) VALUES ('Jan', 'Kowalski'), ('Anna', 'Nowak')
ON CONFLICT DO NOTHING;

-- Administrator (hasło: admin)
INSERT INTO users (username, password, email, role) VALUES (
                                                               'admin',
                                                               '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E',
                                                               'admin@example.com',
                                                               'ROLE_ADMIN'
                                                           ) ON CONFLICT (username) DO NOTHING;