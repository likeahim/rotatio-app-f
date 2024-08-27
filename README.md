# Rotatio Vaadin

## Opis
Jest to frontendowa część aplikacji zbudowana przy użyciu frameworka Vaadin. Komunikuje się z backendową aplikacją poprzez REST API.

## Wymagania systemowe
- Java 17
- Maven lub Gradle
- Node.js (zdefiniowana wersja w pliku `build.gradle`)
- pnpm (globalnie zainstalowany)

## Instalacja
1. Sklonuj repozytorium:
   ```bash
   git clone https://github.com/likeahim/rotatio-app-f.git
   cd vaadin-frontend
2. Upewnij się, że Node.js oraz pnpm są poprawnie zainstalowane:
   Możesz sprawdzić wersję Node.js za pomocą polecenia:
   `node -v`
   Jeśli pnpm nie jest zainstalowany, możesz go zainstalować globalnie:
   `npm install -g pnpm`
3. Zainstaluj zależności i uruchom aplikację:
   `./gradlew bootRun`

## Testowanie
Aby uruchomić testy, użyj:
`./gradlew test`

## Powiązane projekty
Aby uruchomić pełną aplikację, sklonuj i uruchom również repozytorium aplikacji REST API - https://github.com/likeahim/rotatio-app-b

## Licencja
Ten projekt jest licencjonowany na zasadach MIT. Szczegóły znajdują się w pliku LICENSE.
