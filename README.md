# System Tradingowy – trading-system

## Opis projektu
System tradingowy ma na celu obsługę wysokowydajnych operacji giełdowych, takich jak **kupno walorów** (instrumentów finansowych) oraz **wyświetlanie informacji o zleceniach**.  
Aplikacja pełni rolę backendu komunikującego się z zewnętrznymi systemami giełdowymi - brokerami odpowiedzialnymi za realizację zleceń.

Dzięki architekturze opartej na Spring Boot, system został przygotowany do obsługi dużego obciążenia, w tym nawet tysiąca operacji na sekundę, co jest wymagane w intensywnym środowisku tradingowym.

## Główne funkcjonalności
- **Wyszukiwanie instrumentów finansowych**  
  Backend udostępnia API pozwalające na pobieranie informacji o dostępnych walorach.

- **Składanie zleceń kupna**  
  System integruje się z zewnętrznymi giełdami/brokerami w celu realizacji transakcji kupna instrumentów.

- **Wyświetlanie i monitorowanie zleceń**  
  API umożliwia pobieranie statusów zleceń, historii operacji oraz bieżących informacji o transakcjach.

## Architektura i założenia techniczne
- **Spring Boot** jako główna technologia backendowa.
- Integracja z wieloma systemami giełdowymi (brokerami) poprzez dedykowane adaptery.
- API REST służące jako główna warstwa komunikacyjna.

## Cel projektu
Celem jest dostarczenie modułu systemu tradingowego, który będzie stanowił stabilną, wydajną i rozszerzalną podstawę do dalszego rozwoju - w tym realizacji kompleksowych operacji inwestycyjnych, obsługi wielu typów zleceń oraz integracji z kolejnymi giełdami.

## Wymagania
- Java 21+
- Spring Boot 3.5.8+
- Maven 3+
- Docker (opcjonalnie)

## Jak uruchomić
1. mvn clean install
2. mvn spring-boot:run
   lub  
   mvn spring-boot:build-image
   docker run -p 8090:8090 pioneer9500/tradingsystem:t1