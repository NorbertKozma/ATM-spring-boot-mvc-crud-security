package com.nor2code.springboot.thymeleafdemo.dto;

import com.nor2code.springboot.thymeleafdemo.validation.ThousandStep;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class WithdrawRequest {

    @Min(value = 1000, message = "Must be greater than or equal to 1000")
    @Max(value = 150000, message = "Must be less than or equal to 150000")
    @ThousandStep(step = 1000, message = "You can only enter an amount divisible by 1000.")
    private int amount;

    // getter/setter

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
