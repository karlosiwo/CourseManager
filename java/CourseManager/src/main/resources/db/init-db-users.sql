-- ============================================================
-- Role i konta bazodanowe zgodne ze specyfikacją TXT
-- Aplikacja nie łączy się kontem postgres/sysadmin.
-- ============================================================
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'coursemanager_admin') THEN
        CREATE USER coursemanager_admin WITH PASSWORD 'admin_password';
    END IF;
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'coursemanager_developer') THEN
        CREATE USER coursemanager_developer WITH PASSWORD 'developer_password';
    END IF;
    IF NOT EXISTS (SELECT FROM pg_user WHERE usename = 'coursemanager_app') THEN
        CREATE USER coursemanager_app WITH PASSWORD 'app_password';
    END IF;
END $$;

GRANT CONNECT ON DATABASE coursemanager TO coursemanager_admin, coursemanager_developer, coursemanager_app;
GRANT USAGE ON SCHEMA public TO coursemanager_admin, coursemanager_developer, coursemanager_app;

-- Administrator bazy: pełne prawa do struktury i danych w schemacie public.
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO coursemanager_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO coursemanager_admin;
GRANT EXECUTE ON ALL ROUTINES IN SCHEMA public TO coursemanager_admin;

-- Developer: głównie odczyt.
GRANT SELECT ON ALL TABLES IN SCHEMA public TO coursemanager_developer;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO coursemanager_developer;

-- Konto serwerowe aplikacji: ograniczony odczyt/zapis i wykonywanie logiki DB.
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO coursemanager_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO coursemanager_app;
GRANT EXECUTE ON ALL ROUTINES IN SCHEMA public TO coursemanager_app;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO coursemanager_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT ON SEQUENCES TO coursemanager_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO coursemanager_developer;
