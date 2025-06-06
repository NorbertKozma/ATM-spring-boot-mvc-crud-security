package com.nor2code.springboot.thymeleafdemo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
// Ez az annotáció lehetővé teszi, hogy egy számot validálj úgy, hogy csak 1000-rel (vagy más értékkel) osztható legyen.
@Target({ElementType.FIELD}) // csak mezőkön (FIELD) – pl. osztályváltozókon, mint private Integer amount;
@Retention(RetentionPolicy.RUNTIME) // az annotáció futásidőben is elérhető legyen reflektív módon
@Constraint(validatedBy = ThousandStepValidator.class) // melyik osztály végzi a szabály ellenőrzését
public @interface ThousandStep { // Egyedi annotáció definiálása @interface-szel

    int step() default 1000; // Egy paraméter az annotációhoz – megadhatod, milyen értékre legyen osztható az érték (alapértelmezés: 1000).

    String message() default "You can only enter an amount divisible by 1000."; // A hibaüzenet, ha a validáció nem sikerül.

    //define default groups
    public Class<?>[] groups() default {};

    //define default payloads
    public Class<? extends Payload>[] payload() default {};
}
