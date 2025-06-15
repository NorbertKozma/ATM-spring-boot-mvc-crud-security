# ATM REST API valutaárfolyam integrációval

Ez egy Java Spring Boot alapú webalkalmazás, amely lehetővé teszi a felhasználók számára, hogy regisztráljanak, bejelentkezzenek, befizessenek, pénzt vegyenek fel, és figyeljék az egyenlegüket.

## Fő funkciók

- Bejelentkezés (számlaszám és PIN kód)
- Befizetés (deposit) min.:1000 max.:1000000 és egyenleg lekérdezése
- Pénzfelvétel (withdraw) min.:1000 max.:150000 és egyenleg lekérdezése
- Napi limit figyelés (pénzfelvétel maximum 150 000/nap)
- Egyenleg lekérdezés forintban és EURO-ban aktuális árfolyamon
- Admin felület (felhasználók kezelése: új felhasználó regisztráció, felhasználó törlése)

## Belépési adatok

- Számlaszám: 1111111111111111 PIN kód:1234
- Számlaszám: 2222222222222222 PIN kód:2222
- ADMIN számlaszám 3333333333333333 PIN kód:3333

## Technológiák

- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf / JavaScript frontend
- MySQL adatbázis

## Szerző

Készítette: Kozma Norbert

