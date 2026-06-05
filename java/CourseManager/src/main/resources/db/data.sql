-- Kategorie
INSERT INTO categories (name) VALUES ('Programowanie'), ('Bazy danych'), ('DevOps')
ON CONFLICT (name) DO NOTHING;

-- Prowadzący
INSERT INTO instructors (first_name, last_name) VALUES ('Jan', 'Kowalski'), ('Anna', 'Nowak')
ON CONFLICT DO NOTHING;

-- Administrator (hasło: admin)
INSERT INTO users (username, password, email, role) VALUES (
                                                               'admin',
                                                               '$2a$10$awM5GWe4MzCpHOOY/kjJd.59Q8whmjVRHoIwEzxfhuWhVXiMwkA1S',
                                                               'admin@example.com',
                                                               'ROLE_ADMIN'
                                                           ) ON CONFLICT (username) DO NOTHING;