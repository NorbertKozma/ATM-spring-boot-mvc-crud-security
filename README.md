# ATM REST API valutaárfolyam integrációval és webes felülettel (HTML, CSS)

Java Spring Boot alapú webalkalmazás és REST API, amely lehetővé teszi a felhasználók számára a regisztrációt, bejelentkezést, befizetést, pénzfelvételt, valamint az egyenlegük nyomon követését. Az alkalmazás támogatja az alapvető CRUD műveleteket a felhasználói fiókok és tranzakciók kezelésére, továbbá a külső Frankfurter API segítségével árfolyamlekérdezést is biztosít.

Az alkalmazás szerepkör-alapú hozzáférés-vezérlést (RBAC) alkalmaz:
- ADMIN szerepkörrel rendelkező felhasználók kizárólag új felhasználók regisztrációját és meglévő felhasználók törlését végezhetik el.
- ADMIN szerepkörrel rendelkező felhasználók nem jogosultak pénzügyi tranzakciók (befizetés, pénzfelvétel) végrehajtására, és nem férhetnek hozzá az egyenleg lekérdezés funkcióhoz.
- NORMÁL (USER) szerepkörrel rendelkező felhasználók regisztrálhatnak, bejelentkezhetnek, befizethetnek, pénzt vehetnek fel, valamint lekérdezhetik aktuális egyenlegüket forintban és euróban.

Az autentikáció és autorizáció a Spring Security keretrendszerrel valósul meg, ahol a .requestMatchers() és hasRole() konfigurációk szabályozzák a hozzáférést a különböző végpontokhoz.

## Fő funkciók

- Bejelentkezés számlaszám és PIN kód alapján
- Befizetés (deposit), minimum 1 000 Ft, maximum 1 000 000 Ft, 1000-es lépésközzel; a művelet után az egyenleg megjelenítése forintban és euróban, az aktuális árfolyamon
- Pénzfelvétel (withdraw), minimum 1 000 Ft, maximum 150 000 Ft, 1000-es lépésközzel; a művelet után az egyenleg megjelenítése forintban és euróban, az aktuális árfolyamon
- Napi limit figyelése: pénzfelvétel maximum 150 000 Ft/nap
- Admin felület felhasználók kezelésére: új felhasználó regisztrációja, felhasználó törlése

## Belépési adatok (tesztfelhasználók)

| Számlaszám           | PIN kód | Megjegyzés         |
|----------------------|---------|--------------------|
| 1111111111111111     | 1234    | Normál felhasználó |
| 2222222222222222     | 2222    | Normál felhasználó |
| 3333333333333333     | 3333    | ADMIN felhasználó  |

## Technológiák

- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf (HTML, CSS) / JavaScript frontend
- MySQL adatbázis

## Szerző

Készítette: Kozma Norbert

