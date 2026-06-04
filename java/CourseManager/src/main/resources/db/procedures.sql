-- Funkcja licząca wolne miejsca
CREATE OR REPLACE FUNCTION liczba_wolnych_miejsc(p_course_id BIGINT)
    RETURNS INT AS $$
DECLARE
    zajete INT;
    max_miejsc INT;
BEGIN
    SELECT max_seats INTO max_miejsc FROM courses WHERE id = p_course_id;
    SELECT COUNT(*) INTO zajete FROM enrollments WHERE course_id = p_course_id AND status = 'AKTYWNY';
    RETURN max_miejsc - zajete;
END;
$$ LANGUAGE plpgsql;

-- Procedura zapisu na kurs
CREATE OR REPLACE PROCEDURE zapisz_na_kurs(p_user_id BIGINT, p_course_id BIGINT)
    LANGUAGE plpgsql AS $$
DECLARE
    juz_zapisany INT;
BEGIN
    SELECT COUNT(*) INTO juz_zapisany FROM enrollments
    WHERE user_id = p_user_id AND course_id = p_course_id AND status = 'AKTYWNY';

    IF juz_zapisany > 0 THEN
        RAISE EXCEPTION 'Użytkownik jest już zapisany na ten kurs';
    END IF;

    IF liczba_wolnych_miejsc(p_course_id) <= 0 THEN
        RAISE EXCEPTION 'Brak wolnych miejsc na kursie';
    END IF;

    INSERT INTO enrollments (user_id, course_id, status)
    VALUES (p_user_id, p_course_id, 'AKTYWNY');
END;
$$;

-- Procedura anulowania zapisu
CREATE OR REPLACE PROCEDURE anuluj_zapis(p_enrollment_id BIGINT)
    LANGUAGE plpgsql AS $$
BEGIN
    UPDATE enrollments SET status = 'ANULOWANY' WHERE id = p_enrollment_id;
END;
$$;