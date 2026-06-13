-- Kategorie
INSERT INTO categories (name, description, active) VALUES
    ('Programowanie', 'Kursy związane z programowaniem i tworzeniem aplikacji.', TRUE),
    ('Bazy danych', 'Kursy dotyczące projektowania, administracji i optymalizacji baz danych.', TRUE),
    ('DevOps', 'Kursy z automatyzacji, konteneryzacji i wdrażania aplikacji.', TRUE)
ON CONFLICT (name) DO NOTHING;

-- Prowadzący
INSERT INTO instructors (first_name, last_name, email, specialization, bio) VALUES
    ('Jan', 'Kowalski', 'jan.kowalski@example.com', 'Java i Spring', 'Prowadzący zajęcia z programowania aplikacji webowych.'),
    ('Anna', 'Nowak', 'anna.nowak@example.com', 'Bazy danych', 'Specjalistka od SQL, PostgreSQL i projektowania relacyjnych modeli danych.')
ON CONFLICT DO NOTHING;

-- Administrator (hasło: admin)
INSERT INTO users (username, password, email, role) VALUES (
    'admin',
    '$2a$10$awM5GWe4MzCpHOOY/kjJd.59Q8whmjVRHoIwEzxfhuWhVXiMwkA1S',
    'admin@example.com',
    'ROLE_ADMIN'
) ON CONFLICT (username) DO NOTHING;
