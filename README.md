# CourseManager – System zapisów na kursy

## Opis projektu

CourseManager to internetowa aplikacja napisana w języku Java z wykorzystaniem frameworka Spring Boot. Umożliwia zarządzanie kursami, użytkownikami oraz zapisami na kursy.

System obsługuje:

- trzy role użytkowników,
- kontrolę limitu miejsc,
- blokadę podwójnych zapisów,
- sortowanie i filtrowanie kursów,
- raporty i statystyki.

Logika biznesowa zapisów została zaimplementowana w procedurach i funkcjach PL/pgSQL w bazie PostgreSQL.

Aplikacja spełnia wymagania postawione w deklaracji projektowej (MVP oraz wybrane funkcjonalności „Nice to Have”).

---

## Technologie

- Java 17
- Spring Boot 3.2.5
- Spring MVC
- Spring Security
- Spring Data JPA / Hibernate
- Thymeleaf
- Bootstrap 5
- jQuery
- PostgreSQL
- PL/pgSQL
- Docker / Docker Compose
- Maven

---

## Wymagania

- Docker Desktop
- Git
- IntelliJ IDEA (opcjonalnie)
- Wolny port 5432 (lub 5433)
- Wolny port 8080 (lub 8081)

---

# Uruchomienie projektu (Docker)

## 1. Klonowanie repozytorium

```bash
git clone https://github.com/karlosiwo/CourseManager.git
cd CourseManager/java/CourseManager
```

## 2. Uruchomienie kontenerów

```bash
docker compose up -d --build
```

Pierwsze uruchomienie może potrwać kilka minut.

## 3. Sprawdzenie kontenerów

```bash
docker ps
```

Powinny zostać uruchomione:

- `coursemanager-postgres`
- `coursemanager-app`

## 4. Uruchomienie aplikacji

Jeżeli używany jest port 8080:

```text
http://localhost:8080
```

Jeżeli w `docker-compose.yml` ustawiono:

```yaml
ports:
  - "8081:8080"
```

użyj:

```text
http://localhost:8081
```

---

# Logowanie i role

| Login | Hasło | Rola | Uprawnienia |
|---------|---------|---------|---------|
| admin | admin | ROLE_ADMIN | Zarządzanie użytkownikami, kursami, kategoriami, prowadzącymi i raportami |
| użytkownik po rejestracji | własne | ROLE_LIMITED_USER | Przeglądanie kursów i zapisów |
| użytkownik po zmianie roli | własne | ROLE_FULL_USER | Zapisy na kursy oraz zarządzanie kursami |

> Nowo zarejestrowany użytkownik otrzymuje domyślnie rolę `ROLE_LIMITED_USER`.

Role można zmieniać w panelu administratora:

```text
/admin/users
```

---

# Funkcjonalności

## Dla wszystkich użytkowników

- Strona główna
- Rejestracja
- Logowanie
- Lista kursów
- Sortowanie kursów
- Filtrowanie kursów

## ROLE_LIMITED_USER

- przeglądanie kursów,
- przeglądanie szczegółów kursów,
- przeglądanie własnych zapisów.

## ROLE_FULL_USER

Dodatkowo:

- zapis na kurs,
- anulowanie zapisu,
- dodawanie kursów,
- edycja kursów,
- usuwanie kursów.

## ROLE_ADMIN

Dodatkowo:

- zarządzanie użytkownikami,
- zmiana ról,
- raporty,
- pełny dostęp administracyjny.

---

# Kontrola zapisów

System wykorzystuje procedury i funkcje PL/pgSQL.

### Dostępne miejsca

Funkcja:

```sql
liczba_wolnych_miejsc(...)
```

oblicza aktualną liczbę wolnych miejsc.

### Zapis na kurs

Procedura:

```sql
zapisz_na_kurs(...)
```

odpowiada za:

- blokadę podwójnych zapisów,
- sprawdzenie limitu miejsc,
- dodanie zapisu.

### Anulowanie zapisu

Procedura:

```sql
anuluj_zapis(...)
```

zmienia status zapisu na:

```text
ANULOWANY
```

dzięki czemu historia zapisów pozostaje zachowana.

---

# REST API

## Walidacja kategorii

```http
GET /api/categories/validate?name=java
```

## Dostępność miejsc

```http
GET /api/courses/{id}/availability
```

Przykładowa odpowiedź:

```json
{
  "availablePlaces": 5
}
```

---

# Walidacja

Aplikacja wykorzystuje:

### Walidację po stronie klienta

- HTML5
- JavaScript

### Walidację po stronie serwera

- `@NotBlank`
- `@Email`
- `@Size`
- `@Future`
- inne adnotacje Bean Validation

---

# Struktura projektu

```text
CourseManager
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── src
│   ├── main
│   │   ├── java/com/coursemanager
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── exception
│   │   │   ├── model/entity
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── validation
│   │   └── resources
│   │       ├── application.properties
│   │       ├── db
│   │       │   ├── schema.sql
│   │       │   ├── procedures.sql
│   │       │   └── data.sql
│   │       ├── templates
│   │       └── static
│   └── test
└── README.md
```

---

# Konfiguracja PostgreSQL

Docker uruchamia bazę danych z parametrami:

```text
Database: coursemanager
User: postgres
Password: mysecretpassword
Port: 5432
```

Podczas pierwszego uruchomienia wykonywane są:

```text
schema.sql
procedures.sql
data.sql
```

---

# Uruchomienie bez Dockera

1. Utwórz bazę danych `coursemanager`.
2. Wykonaj ręcznie:
   - `schema.sql`
   - `procedures.sql`
   - `data.sql`
3. Skonfiguruj `application.properties`.
4. Uruchom:

```java
CourseManagerApplication
```

5. Otwórz:

```text
http://localhost:8080
```

---

# Najczęstsze problemy

| Problem | Rozwiązanie |
|----------|----------|
| Connection refused | Uruchom PostgreSQL lub kontener Docker |
| Błąd logowania admina | Sprawdź poprawność hasha BCrypt w bazie |
| Brak procedur | Wykonaj `procedures.sql` |
| Port 8080 zajęty | Użyj mapowania `8081:8080` |
| Port 5432 zajęty | Użyj mapowania `5433:5432` |

---

# Swagger

Dokumentacja OpenAPI:

```text
http://localhost:8080/swagger-ui.html
```

lub

```text
http://localhost:8081/swagger-ui.html
```

---

# Autorzy

- Jakub Wierciszewski (117268)
- Karol Ziemak (117292)
- Michał Szwabowicz (111112)
- 
Projekt wykonany w ramach przedmiotu **Programowanie Zespołowe (PS7)**.

---

# Licencja

Projekt udostępniony wyłącznie w celach edukacyjnych.

---

Ostatnia aktualizacja: czerwiec 2026
