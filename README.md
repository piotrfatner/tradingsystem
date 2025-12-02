# System Tradingowy – trading-system

## Opis projektu
System tradingowy ma na celu obsługę wysokowydajnych operacji giełdowych, takich jak **kupno walorów** (instrumentów finansowych) oraz **wyświetlanie informacji o zleceniach**.  
Aplikacja pełni rolę backendu komunikującego się z zewnętrznymi systemami giełdowymi - brokerami odpowiedzialnymi za realizację zleceń.

Dzięki architekturze opartej na Spring Boot, system został przygotowany do obsługi dużego obciążenia, w tym nawet tysiąca operacji na sekundę, co jest wymagane w intensywnym środowisku tradingowym.

## Główne funkcjonalności
### 1. Logika biznesowa
- **Zarządzanie zleceniami:** umożliwia tworzenie, wysyłanie oraz śledzenie zleceń (Kupno/Sprzedaż).
- **Instrumenty i dane cenowe:** pobiera dane rynkowe w czasie rzeczywistym (instrumenty i ceny) z zewnętrznego systemu GPW.
- **Przetwarzanie w tle:** scheduler `OrderStatusScheduler` cyklicznie sprawdza status wysłanych zleceń i aktualizuje je lokalnie.

---

### 2. Integracje zewnętrzne
- **REST Client:** nowoczesny klient Spring 6 (`RestClient`) z **Apache HttpClient 5** do komunikacji z API GPW, z konfiguracją timeoutów i poolingiem połączeń.
- **WireMock:** moduł `wiremock_dev` symuluje API GPW podczas developmentu i testów integracyjnych, umożliwiając testowanie systemu w izolacji.

---

### 3. Przechowywanie danych
- **Baza danych:** H2 (dev/test) lub PostgreSQL z **Spring Data JPA**.
- **Cache:** `@Cacheable` dla danych instrumentów; `CacheEvictionScheduler` odświeża cache co 15 minut.

---

### 4. Architektura zdarzeniowa
- **Kafka:** publikowanie zdarzeń (np. `OrderFilled`) na topic Kafkowy, umożliwiające asynchroniczną komunikację z innymi systemami.

---

## Architektura i założenia techniczne
### 1. Nowoczesne funkcje Spring
- Użycie **`RestClient`** zamiast przestarzałego `RestTemplate`.
- Wykorzystanie **Java Records** dla konfiguracji (`RestClientConfigSettings`, `AccountInfo`).

---

### 2. Solidna konfiguracja
- **Type-safe configuration:** `@ConfigurationProperties` porządkuje i waliduje ustawienia.
- **Odporność:** explicit timeouty oraz connection pooling w kliencie HTTP zapewniają stabilność.

---

### 3. Separacja odpowiedzialności
- **Scheduler statusów:** `OrderStatusScheduler` obsługuje aktualizacje asynchronicznie, odciążając proces tworzenia zleceń.
- **Testy integracyjne:** dedykowany setup z WireMock (`OrderStatusSchedulerIntegrationTest`).

---

### 4. Optymalizacja wydajności
- **Cache danych rynkowych:** zmniejsza obciążenie zewnętrznego API i skraca czas odpowiedzi.
- **Planowe odświeżanie cache:** co 15 minut dla zachowania aktualności danych.

---

### 5. Dokumentacja API
- **OpenAPI (Swagger):** kontroler `InstrumentController` zawiera adnotacje umożliwiające automatyczne generowanie dokumentacji.

---

### 6. Komunikacja zdarzeniowa
- **Kafka:** wysłanie zdarzenia `OrderFilled` pozwala na łatwe rozszerzanie architektury (np. serwis powiadomień) bez zmian w logice zleceń.

## Cel projektu
Celem jest dostarczenie modułu systemu tradingowego, który będzie stanowił stabilną, wydajną i rozszerzalną podstawę do dalszego rozwoju - w tym realizacji kompleksowych operacji inwestycyjnych, obsługi wielu typów zleceń oraz integracji z kolejnymi giełdami.

## Wymagania
- Java 21+
- Spring Boot 3.5.8+
- Maven 3+
  Docker & Docker Compose (opcjonalnie — do infrastruktury pomocniczej)
- Kafka (wymagane dla przetwarzania eventów)
- PostgreSQL (opcjonalnie — dla profili innych niż H2)
- WireMock (opcjonalnie — symulacja API GPW w testach Integracyjnych) / Dla apliacji należy uruchomić standalone Wiremocka z https://github.com/piotrfatner/wiremock_dev.git

## Jak uruchomić
1. mvn clean install
2. mvn spring-boot:run
   lub  
   mvn spring-boot:build-image
   docker run -p 8090:8090 pioneer9500/tradingsystem:t1
3. WireMock service z preparowanymi danymi: https://github.com/piotrfatner/wiremock_dev.git
4. docker run -p 9092:9092 apache/kafka:latest

## Endpointy
http://localhost:8090/swagger-ui/index.html

## Baza danych - skrypt
- src/main/resources/schema.sql