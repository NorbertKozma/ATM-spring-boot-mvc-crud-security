package com.nor2code.springboot.thymeleafdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration //Ez az osztály konfigurációs osztályként van megjelölve – a Spring felismeri, és használja a beállításokat futás közben.
public class DemoSecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    // add support for JDBC ... no more hardcoded users :-)
    @Bean //Bean-t regisztrálunk, ami felhasználókat tud lekérni JDBC-n keresztül.
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        // Létrehozunk egy JDBC-alapú UserDetailsManager példányt, ami az adatbázist használja.
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery( // Azt mondjuk meg, milyen SQL-lekérdezéssel lehet a felhasználókat betölteni.
                "select user_id, pw, active from members where user_id=?");

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");

        return jdbcUserDetailsManager; // A Spring Security ezt fogja használni a hitelesítéshez.
    }

    // HTTP biztonsági beállítások
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Ez a metódus a SecurityFilterChain-t hozza létre, amely leírja, milyen URL-t ki érhet el.

            http.authorizeHttpRequests(configuer ->
                        configuer
                                .requestMatchers("/showMyLoginPage","/","/css/**", "/js/**").permitAll() // A /css és /js mappákban található fájlok mindenki számára elérhetők.
                                //.requestMatchers("/").hasRole("EMPLOYEE") // Ezeket az útvonalakat csak az EMPLOYEE szereppel rendelkező felhasználók érhetik el.
                                .requestMatchers("/users/deposit").hasRole("EMPLOYEE")
                                .requestMatchers("/users/withdraw").hasRole("EMPLOYEE")
                                .requestMatchers("/users/menu").hasRole("EMPLOYEE")
                                .requestMatchers("/users/admin").hasRole("ADMIN") // Az /users/admin csak az ADMIN szerepkörhöz tartozóknak elérhető.

                                .anyRequest().authenticated() // Minden más kéréshez bejelentkezés szükséges.
                )
                .formLogin(form -> // Beléptetés (form alapú)
                        form
                                .loginPage("/showMyLoginPage") // Saját belépési oldal: a /showMyLoginPage-re irányít.
                                .loginProcessingUrl("/authenticateTheUser") // Ez az URL fogadja a POST kérést (jelszó + felhasználónév), automatikusan elintézi a hitelesítést.
                                // .defaultSuccessUrl("/users/list", true)
                                .successHandler(successHandler) // Saját sikerkezelőt használunk – pl. szerepkör alapján eltérő URL-re dobhat.
                                .permitAll() // A bejelentkezési oldal bárki számára elérhető.
                )
                .logout(logout -> logout.permitAll() // A kijelentkezés mindenki számára engedélyezett.
                .logoutSuccessUrl("https://atm-spring-boot-mvc-crud-security-production.up.railway.app/showMyLoginPage") // Kijelentkezés után ide visz
                .permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied") // Ha egy jogosulatlan felhasználó próbál elérni valamit, a rendszer a /access-denied oldalra dobja.
                );

        return http.build(); // Visszaadjuk a konfigurált biztonsági láncot.
    }
}
