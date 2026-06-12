# CourseManager – system zapisów na kursy

## Opis projektu

CourseManager to aplikacja internetowa napisana w Javie z użyciem Spring Boot. System umożliwia zarządzanie kursami, kategoriami, prowadzącymi, użytkownikami oraz zapisami na kursy.

Projekt zawiera panel użytkownika, panel administratora, logowanie z rolami, operacje CRUD, REST API, dokumentację Swagger/OpenAPI oraz logikę zapisów opartą częściowo o procedury i funkcje PostgreSQL.

## Technologie

- Java 17
- Spring Boot 3.2.5
- Spring MVC
- Spring Security
- Spring Data JPA / Hibernate
- Thymeleaf
- Bootstrap 5
- jQuery
- PostgreSQL 16
- PL/pgSQL
- Swagger / OpenAPI
- Docker / Docker Compose
- Maven

## Wymagania

- Docker Desktop
- Git
- IntelliJ IDEA lub inne IDE
- wolny port 8081 dla aplikacji
- wolny port 5433 dla bazy danych

## Uruchomienie projektu przez Docker

### 1. Przejście do katalogu aplikacji

```bash
cd java/CourseManager
```

### 2. Uruchomienie kontenerów

```bash
docker compose up --build
```

Po uruchomieniu powinny działać kontenery:

- `coursemanager-postgres`
- `coursemanager-app`

### 3. Adres aplikacji

```text
http://localhost:8081
```

### 4. Reset bazy po zmianach struktury

Jeżeli projekt był wcześniej uruchamiany, skrypty SQL mogą się nie wykonać ponownie. Wtedy trzeba usunąć stary wolumen bazy:

```bash
docker compose down -v
docker compose up --build
```

## Logowanie i role

| Login | Hasło | Rola | Uprawnienia |
|---|---|---|---|
| `admin` | `admin` | `ROLE_ADMIN` | Zarządzanie użytkownikami, kursami, kategoriami, prowadzącymi i raportami |
| użytkownik po rejestracji | własne | `ROLE_LIMITED_USER` | Przeglądanie kursów i własnych zapisów |
| użytkownik po zmianie roli | własne | `ROLE_FULL_USER` | Zarządzanie kursami, kategoriami, prowadzącymi, zapisami i udostępnieniami |

Nowo zarejestrowany użytkownik otrzymuje domyślnie rolę `ROLE_LIMITED_USER`.

Zmiana ról jest dostępna w panelu administratora:

```text
/admin/users
```

## Funkcjonalności

### Dla niezalogowanych

- strona główna,
- rejestracja,
- logowanie,
- publiczny widok udostępnionego kursu przez link.

### Dla użytkowników

- lista kursów,
- szczegóły kursu,
- sortowanie i filtrowanie kursów,
- zapamiętywanie sortowania w cookies,
- przeglądanie własnych zapisów,
- zapis na kurs,
- anulowanie zapisu,
- komunikat o braku wolnych miejsc.

### Dla pełnych użytkowników i administratorów

- CRUD kursów,
- CRUD kategorii,
- CRUD prowadzących,
- udostępnianie kursu innemu użytkownikowi,
- generowanie publicznego linku do kursu.

### Dla administratora

- lista użytkowników,
- zmiana ról,
- raporty,
- raport popularnych kursów.

## Kontrola zapisów

System wykorzystuje procedury i funkcje PL/pgSQL.

### Dostępne miejsca

Funkcja:

```sql
liczba_wolnych_miejsc(...)
```

oblicza aktualną liczbę wolnych miejsc na kursie.

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

zmienia status zapisu na `ANULOWANY`, dzięki czemu historia zapisów pozostaje zachowana.

### Audyt zmian

Trigger:

```sql
trg_enrollment_status
```

zapisuje zmianę statusu zapisu do tabeli `audit_log`.

## Udostępnianie kursów

Udostępnianie kursów pozwala przekazać informację o kursie innemu użytkownikowi albo osobie niezalogowanej przez link.

Dostępne są dwa tryby:

- udostępnienie konkretnemu użytkownikowi,
- wygenerowanie publicznego linku.

Publiczny link ma postać:

```text
/public/share/{token}
```

Udostępnienie nie oznacza zapisu na kurs ani prawa do jego edycji.

## REST API

Projekt zawiera REST API dla głównych zasobów:

```text
/api/courses
/api/categories
/api/instructors
/api/enrollments
```

Przykładowe endpointy:

```text
GET    /api/courses
POST   /api/courses
PUT    /api/courses/{id}
DELETE /api/courses/{id}

GET    /api/categories
POST   /api/categories
PUT    /api/categories/{id}
DELETE /api/categories/{id}

GET    /api/instructors
POST   /api/instructors
PUT    /api/instructors/{id}
DELETE /api/instructors/{id}

GET    /api/enrollments
POST   /api/enrollments
DELETE /api/enrollments/{id}
```

Dostępność miejsc:

```text
GET /api/courses/{id}/availability
```

Przykładowa odpowiedź:

```json
{
  "availableSeats": 5
}
```

Swagger UI:

```text
http://localhost:8081/swagger-ui/index.html
```

## Walidacja

Aplikacja wykorzystuje walidację formularzy po stronie serwera i widoków Thymeleaf.

Użyte przykłady walidacji:

- `@NotBlank`
- `@NotNull`
- `@Size`
- `@Email`
- `@Min`
- `@Max`
- `@FutureOrPresent`
- własna walidacja unikalności kategorii

## Struktura projektu

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
│   │   │   ├── controller/rest
│   │   │   ├── dto
│   │   │   ├── exception
│   │   │   ├── mapper
│   │   │   ├── model/entity
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── validation
│   │   └── resources
│   │       ├── db
│   │       │   ├── schema.sql
│   │       │   ├── procedures.sql
│   │       │   ├── init-db-users.sql
│   │       │   └── data.sql
│   │       ├── templates
│   │       └── application.properties
│   └── test
└── README.md
```

## Konfiguracja PostgreSQL

Docker uruchamia bazę danych z parametrami:

| Parametr | Wartość |
|---|---|
| Database | `coursemanager` |
| Admin kontenera | `postgres` |
| Hasło admina kontenera | `mysecretpassword` |
| Port lokalny | `5433` |
| Konto aplikacji | `coursemanager_app` |
| Hasło konta aplikacji | `app_password` |

Aplikacja łączy się z bazą przez konto `coursemanager_app`, a nie przez konto `postgres`.

Podczas pierwszego uruchomienia wykonywane są:

- `schema.sql`,
- `procedures.sql`,
- `init-db-users.sql`,
- `data.sql`.

## Najczęstsze problemy

| Problem | Rozwiązanie |
|---|---|
| Nie działa login `admin` / `admin` | Sprawdź, czy aplikacja działa na `http://localhost:8081` i czy baza została zresetowana po zmianach. |
| Brak nowych tabel albo procedur | Wykonaj `docker compose down -v`, a potem `docker compose up --build`. |
| Port 8081 zajęty | Zmień mapowanie portów w `docker-compose.yml`. |
| Port 5433 zajęty | Zmień mapowanie portu PostgreSQL w `docker-compose.yml`. |
| Błąd połączenia z bazą przy uruchomieniu lokalnym | Dla uruchomienia bez Dockera zmień host bazy z `postgresql` na `localhost`. |

## Uwagi końcowe

Projekt realizuje główne wymagania aplikacji: role, logowanie, CRUD MVC, REST API, zapisy na kursy, udostępnianie kursów, walidację, procedury bazodanowe, trigger, Swagger oraz dokumentację.

## Autorzy

Jakub Wierciszewski (117268)  
Karol Ziemak (117292)  
Michał Szwabowicz (111112)

Projekt wykonany w ramach przedmiotu "Programowanie aplikacji WWW w języku Java" oraz "Systemy Baz Danych" (PS7).
