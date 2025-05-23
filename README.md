# Dashboard Studencki

## Opis Projektu

**Dashboard Studencki** to aplikacja webowa stworzona w celu pomocy studentom w organizacji nauki i zarządzaniu zasobami akademickimi. Projekt jest rozwijany z wykorzystaniem Java Spring Boot oraz Thymeleaf. Aplikacja umożliwia użytkownikom zarządzanie zadaniami, notatkami, korzystanie z minutnika Pomodoro oraz przeglądanie podstawowych statystyk dotyczących ich aktywności.

## Technologie

* **Backend:** Java 17, Spring Boot 3.x
    * Spring MVC
    * Spring Data JPA (z Hibernate)
    * Spring Security
* **Frontend:** Thymeleaf, HTML, CSS (Bootstrap 4)
* **Baza Danych:** H2 Database (aktualnie skonfigurowana jako baza w pamięci - dane są czyszczone po każdym restarcie aplikacji)
* **System Budowania:** Apache Maven
* **Inne:** Lombok

## Aktualne Funkcjonalności

1.  **Uwierzytelnianie Użytkownika:**
    * Strona logowania.
    * Możliwość wylogowania.
    * Zabezpieczenie dostępu do zasobów aplikacji.
2.  **Rejestracja Nowych Użytkowników:**
    * Formularz rejestracyjny pozwalający na samodzielne zakładanie kont.
    * Walidacja danych wejściowych.
    * Nowi użytkownicy otrzymują domyślną rolę `ROLE_USER`.
3.  **Zarządzanie Zadaniami (Tasks):**
    * Tworzenie, odczyt, aktualizacja i usuwanie (CRUD) zadań.
    * Każde zadanie może mieć tytuł, opis, termin wykonania, priorytet i status.
    * Wyszukiwanie zadań po fragmencie tekstu w tytule lub opisie.
    * Filtrowanie zadań po statusie i priorytecie.
    * Sortowanie listy zadań (po tytule, terminie, priorytecie, statusie).
    * Zadania są przypisane do zalogowanego użytkownika.
4.  **Zarządzanie Notatkami (Notes):**
    * Tworzenie, odczyt, aktualizacja i usuwanie (CRUD) notatek.
    * Każda notatka może mieć tytuł (opcjonalny) i treść.
    * Wyszukiwanie notatek po fragmencie tekstu w tytule lub treści.
    * Sortowanie listy notatek (po tytule, dacie utworzenia, dacie modyfikacji).
    * Notatki są przypisane do zalogowanego użytkownika.
    * Notatki wyświetlane są w formie kart.
5.  **Minutnik Pomodoro:**
    * Prosty minutnik Pomodoro zaimplementowany po stronie klienta (JavaScript).
    * Możliwość uruchomienia sesji pracy (25 minut) i przerw (5 minut krótka, 15 minut długa).
    * Przyciski Start, Pauza, Reset.
    * Licznik ukończonych sesji Pomodoro.
6.  **Dashboard (Strona Główna):**
    * Wyświetlanie powitania dla zalogowanego użytkownika.
    * Podstawowe statystyki:
        * Całkowita liczba zadań.
        * Liczba zadań do zrobienia.
        * Liczba zadań w trakcie.
        * Liczba zadań ukończonych.
        * Całkowita liczba notatek.
    * Szybkie linki do głównych modułów aplikacji.

## Jak Uruchomić Projekt

1.  **Wymagania:**
    * Java Development Kit (JDK) w wersji 17 lub nowszej.
    * Apache Maven.
2.  **Klonowanie Repozytorium (jeśli projekt jest w repozytorium Git):**
    ```bash
    git clone <URL_do_repozytorium>
    cd dashboardstudencki
    ```
3.  **Budowanie i Uruchamianie za pomocą Mavena:**
    Otwórz terminal w głównym katalogu projektu (tam, gdzie znajduje się plik `pom.xml`) i wykonaj polecenie:
    ```bash
    mvn spring-boot:run
    ```
4.  **Dostęp do Aplikacji:**
    Po pomyślnym uruchomieniu aplikacja będzie dostępna w przeglądarce pod adresem: `http://localhost:8080/`
5.  **Konsola H2 (opcjonalnie, jeśli włączona w `application.properties`):**
    Jeśli konsola H2 jest włączona, będzie dostępna pod adresem: `http://localhost:8080/h2-console`
    * JDBC URL: `jdbc:h2:mem:dashboarddb` (dla bazy w pamięci) lub `jdbc:h2:file:./data/dashboarddb` (jeśli skonfigurowano zapis do pliku)
    * User Name: `sa`
    * Password: (puste)

## Domyślne Konto Administratora

Przy pierwszym uruchomieniu aplikacji (lub jeśli baza danych jest czyszczona), tworzone jest domyślne konto administratora:
* **Login:** `admin`
* **Hasło:** `admin123`

## Dalszy Rozwój (Pomysły)

* Implementacja modułu Kalendarza z importem plików `.ics`.
* Rozbudowa notatek o edytor WYSIWYG.
* Wizualny widok Kanban dla zadań (z funkcją drag-and-drop).
* Organizator materiałów do nauki.
* Fiszki (Flashcards) do nauki.
* Rozbudowa statystyk i dodanie wykresów.
