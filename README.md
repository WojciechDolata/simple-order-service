# simple-order-service

Demo do talku **„Spock: testy, które przetrwają refaktor”**: małe zamówienia z kuponami,
testowane od zewnątrz (black-box) i od środka (white-box), żeby zobaczyć, co przeżyje refaktor.

```
POST /orders ──▶ order-service ──▶ HTTP 201/4xx/5xx
                     │  ├─▶ GET /coupons/{code}          (coupon-service, w testach WireMock)
                     │  ├─▶ INSERT INTO orders           (Postgres)
                     │  └─▶ Kafka: order-events          (klucz = orderId)
```

## Struktura (DDD / porty i adaptery)

```
pl.wdolata.simpleorderservice
├── api                 kontroler, DTO, mapowanie request/response, obsługa błędów
├── domain              logika biznesowa, niezależna od frameworków I/O
│   ├── PlaceOrderService, OrderIdGenerator
│   ├── commands, model (Order, OrderItem, Coupon, valueobject, event), exception
│   └── ports           OrderStore, CouponProvider, OrderEventStream
├── adapters            implementacje portów, nie zależą od siebie nawzajem
│   ├── database/orders Spring Data JDBC, encja, konwertery jsonb, MapStruct
│   ├── kafka/publishers/orderevents
│   └── coupon          RestClient do zewnętrznego serwisu kuponów
└── config
```

Kierunek zależności pilnuje `ArchitectureSpec` (ArchUnit): `domain` nie zna `adapters` ani `api`,
adaptery nie zależą od siebie, kontrolery leżą w `api`.

## Testy

| Spec | Rodzaj | Co sprawdza |
|---|---|---|
| `api/PlaceOrderSpec` | black-box | zamówienie z kuponem i bez: odpowiedź HTTP, wiersz w bazie, request do coupon-service, wiadomość na Kafce (klucz + payload) |
| `api/PlaceOrderCouponErrorsSpec` | black-box | wygasły / nieznany kupon, coupon-service 500/503/timeout: kod błędu, pusta baza, brak wiadomości |
| `api/PlaceOrderValidationSpec` | black-box | tabela `where:` ze złymi requestami: 400, nic nie zapisano ani nie wysłano |
| `domain/PlaceOrderServiceWhiteBoxSpec` | white-box | ta sama funkcja z `Mock()` na portach (psuje się po refaktorze) |
| `adapters/database/orders/OrderEntityMapperSpec` | white-box | mapowanie encja ⇄ model domenowy w obie strony, `placedAt` ⇄ `createdAt` |
| `adapters/database/orders/OrderItemsConvertersSpec` | white-box | konwertery jsonb (`PGobject`): zapis, odczyt, round trip, zły JSON |
| `adapters/database/orders/OrderStoreImplSpec` | white-box | store z zamockowanym repozytorium i prawdziwym mapperem |
| `adapters/database/orders/OrderPersistenceSpec` | white-box + prawdziwa baza | repozytorium na realnym schemacie: jsonb, kolumny, insert-only (duplikat id = błąd) |
| `ArchitectureSpec` | architektura | kierunek zależności między warstwami |

Black-box mockuje tylko świat zewnętrzny (WireMock) i niedeterminizm (zegar, generator id).
Postgres i Kafka to prawdziwe kontenery (Testcontainers), a migracje Flyway odpalają się jak na produkcji.

Infrastruktura testowa jest w `src/test/groovy/.../support` (WireMock, listener Kafki, JSON fixtures),
a fixtures jako beany Springa w `.../fixtures` (nazwa `<Callee>Fixture`).

## Uruchomienie

Wymagane: Java 21+, Maven, Docker.

```bash
mvn clean verify
```

Projekt ma lokalne `.mvn/settings.xml`, więc zależności idą wyłącznie z Maven Central.

## Gałęzie

- `main`: gruby `PlaceOrderService`, wszystkie zestawy zielone.
- `refactor-pt1`: po refaktorze bez testów (`DiscountCalculator`, `OrderFactory`, `OrderPlacedEventFactory`):
  black-box, testy adapterów i `ArchitectureSpec` zielone bez zmian w `src/test`, white-box domeny czerwony.
  Prompt użyty do refaktoru: [`demo/refactor-prompt.md`](demo/refactor-prompt.md).
- `refactor-pt2`: to samo co `refactor-pt1` plus naprawiony `PlaceOrderServiceWhiteBoxSpec` i nowe testy white-box
  (kalkulator rabatu, fabryki). Wszystko zielone.
- `feature-coupon`: nad `refactor-pt2`, minimalna wartość zamówienia dla kuponu z pola `minimalValue`
  coupon-service ([`demo/feature.md`](demo/feature.md)). Wszystko zielone.
