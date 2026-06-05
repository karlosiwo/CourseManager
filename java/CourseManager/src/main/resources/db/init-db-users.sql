-- ============================================================
-- Tworzenie użytkownika dla aplikacji (z ograniczonymi prawami)
-- ============================================================
DO $$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'coursemanager_app') THEN
            CREATE USER coursemanager_app WITH PASSWORD 'app_password';
        END IF;
    END $$;

-- Nadanie uprawnień (dostęp tylko do bazy 'coursemanager')
GRANT CONNECT ON DATABASE coursemanager TO coursemanager_app;

-- Uprawnienia na wszystkich tabelach w schemacie public (odczyt + zapis)
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO coursemanager_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO coursemanager_app;

-- Domyślne uprawnienia dla nowych tabel (jeśli jakieś zostaną dodane)
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO coursemanager_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO coursemanager_app;