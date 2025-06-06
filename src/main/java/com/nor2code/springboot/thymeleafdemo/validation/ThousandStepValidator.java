package com.nor2code.springboot.thymeleafdemo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
// nem lehet primitív típus, mindig referenciatípus (például Integer, String, BigDecimal, stb.)
public class ThousandStepValidator implements ConstraintValidator<ThousandStep, Integer> { // a Bean Validation autoboxing-gal automatikusan Integer-ként kezeli a validátorban a int amount mezőt

    private int step;

    @Override
    public void initialize(ThousandStep constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        // Itt olvassuk ki az annotációban beállított lépést
        this.step = constraintAnnotation.step();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return value == null || value % step == 0;
    }
}
