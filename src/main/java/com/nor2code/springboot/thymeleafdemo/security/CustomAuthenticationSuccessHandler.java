package com.nor2code.springboot.thymeleafdemo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
// Ez az osztály azt csinálja, hogy a bejelentkezés után a felhasználó szerepköre alapján meghatározza, hova legyen átirányítva.
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, // Ez a metódus hívódik meg, amikor a felhasználó sikeresen bejelentkezett. HTTP kérés
                                        HttpServletResponse response, //  HTTP válasz
                                        Authentication authentication) throws IOException { // a belépett felhasználó hitelesítési adatai
        // Szerepkör lekérdezése, Lekéri a belépett felhasználó szerepköreit (jogosultságait), amik GrantedAuthority objektumokként vannak tárolva.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectURL = "/showMyLoginPage"; // Alapértelmezett átirányítási URL beállítása a gyökér (/) oldalra.

        for (GrantedAuthority authority : authorities) { // Végigiterálunk a felhasználó szerepkörein.
            String role = authority.getAuthority(); // A getAuthority() visszaadja a szerepkör nevét, pl. "ROLE_ADMIN", "ROLE_EMPLOYEE".

            if (role.equals("ROLE_ADMIN")) { // Ha a szerepkör ADMIN, akkor az átirányítás a /users/admin oldalra történik.
                redirectURL = "/users/admin";
                break; // break: kilépünk a ciklusból, mert megtaláltuk a megfelelő szerepkört.
            }

            if (role.equals("ROLE_EMPLOYEE")) { // Ha a szerepkör EMPLOYEE, akkor a felhasználót a /users/menu oldalra irányítja.
                // redirectURL = "/users/list";
                redirectURL = "/users/menu";
                break;
            }


        }

        response.sendRedirect(redirectURL); // A felhasználót az előzőleg meghatározott URL-re átirányítja.
    }
}

/*
Összefoglalva:

    Ez az osztály azt csinálja, hogy a bejelentkezés után a felhasználó szerepköre alapján meghatározza, hova legyen átirányítva.

    Adminisztrátor a /users/admin oldalra,

    Munkatárs (employee) a /users/menu oldalra,

    Más esetben az alapértelmezett / oldalra.
*/