package com.nor2code.springboot.thymeleafdemo.controller;

import com.nor2code.springboot.thymeleafdemo.dto.DepositRequest;
import com.nor2code.springboot.thymeleafdemo.dto.NewMemberRequest;
import com.nor2code.springboot.thymeleafdemo.dto.WithdrawRequest;
import com.nor2code.springboot.thymeleafdemo.entity.User;
import com.nor2code.springboot.thymeleafdemo.service.ExchangeRateService;
import com.nor2code.springboot.thymeleafdemo.service.UserService;
import com.nor2code.springboot.thymeleafdemo.service.WithdrawalsService;
import com.nor2code.springboot.thymeleafdemo.util.FormatUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.math.RoundingMode;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    //@Autowired //így is lehetne injektálni, konstruktoros injektálás (ajánlott)
    private WithdrawalsService withdrawalsService;
    private ExchangeRateService exchangeRateService;


    @Autowired
    public UserController(UserService theUserService, WithdrawalsService theWithdrawalsService, ExchangeRateService theExchangeRateService) {
        userService = theUserService;
        withdrawalsService = theWithdrawalsService;
        exchangeRateService = theExchangeRateService;
    }


    @GetMapping("/menu")
    public String showMenu() {

        return "employees/atm-user-menu-e";  // ez tölti be a menu.html-t
    }

    @GetMapping("/admin")
    public String showAdminMenu() {

        return "employees/atm-admin-menu";  // ez tölti be a menu.html-t
    }

    @PostMapping("/deposit")
    @ResponseBody
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositRequest request, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        User theUser = userService.findById(principal.getName());
        if (theUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        int depositAmount = request.getAmount();
        theUser.setAmount(theUser.getAmount() + depositAmount);

        try {
            userService.save(theUser);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving deposit", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BigDecimal rate;
        try {
            rate = exchangeRateService.getHufToEurRate();
        } catch (Exception e) {
            rate = BigDecimal.ZERO;
        }

        BigDecimal eurBalance = BigDecimal.valueOf(theUser.getAmount())
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);

        String responseMessage = "Your new balance is:\n\n" + FormatUtils.formatAmount(theUser.getAmount(), 0) + " HUF\n\n";
        responseMessage += (rate.compareTo(BigDecimal.ZERO) != 0)
                ? FormatUtils.formatAmount(eurBalance, 2) + " EUR"
                : "\nEUR balance:\nError when querying the exchange rate";

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    /* régi
    @PostMapping("/deposit")
    @ResponseBody
    public ResponseEntity<String> deposit(@Valid @RequestBody DepositRequest request, Model theModel, Principal principal) {

        User theUser = userService.findById(principal.getName());

        if (theUser == null) { // itt nincs jelentősége, mert van iylen felhasználó a beléptetés miatt mindig
            throw new RuntimeException("Employee id is not find - " + principal.getName());
        }
        int depositAmount = request.getAmount();


        // Csak az amount mezőt módosítjuk → ez PATCH viselkedés
        theUser.setAmount(theUser.getAmount() + depositAmount);

        // Mentés
        userService.save(theUser);

        BigDecimal rate = exchangeRateService.getHufToEurRate();
        BigDecimal eurBalance = BigDecimal.valueOf(theUser.getAmount()).multiply(rate).setScale(2,RoundingMode.HALF_UP);

        String responseMessage = "Your new balance is:\n\n" + FormatUtils.formatAmount(theUser.getAmount(),0) + " HUF\n\n";
        responseMessage += (rate.compareTo(BigDecimal.ZERO) != 0) //BigDecimal-t pedig nem szabad !=-tel hasonlítani, helyette használd a compareTo
                ? FormatUtils.formatAmount(eurBalance,2) + " EUR" //formatAmount statikus metódus: segédfüggvény, azaz nincs szükség példány létrehozására. Gyakran hívod, csak adatot kapsz vissza, nincs állapot (pl. szám formázás, dátum konvertálás stb.)
                : "\nEUR balance:\nError when querying the exchange rate";

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
*/
    @PostMapping("/withdraw")
    @ResponseBody
    public ResponseEntity<String> withdrawal(@Valid @RequestBody WithdrawRequest request, Model theModel, Principal principal) {

        User theUser = userService.findById(principal.getName());
        //String response;

        if (theUser == null) {
            throw new RuntimeException("Employee id is not find - " + principal.getName());
        }

        //int amountToWithdraw = Integer.parseInt(theAmount);
        int amountToWithdraw = request.getAmount();
        String userId = principal.getName();

        BigDecimal rate = exchangeRateService.getHufToEurRate();
        BigDecimal eurBalance = BigDecimal.valueOf(theUser.getAmount()).multiply(rate).setScale(2,RoundingMode.HALF_UP);

        String responseMessage = withdrawalsService.withdraw(userId, amountToWithdraw);
        responseMessage += (rate.compareTo(BigDecimal.ZERO) != 0) //BigDecimal-t pedig nem szabad !=-tel hasonlítani, helyette használd a compareTo
                ? "\n\nEuro Balance: " + FormatUtils.formatAmount(eurBalance,2) + " EUR" //formatAmount statikus metódus: segédfüggvény, azaz nincs szükség példány létrehozására. Gyakran hívod, csak adatot kapsz vissza, nincs állapot (pl. szám formázás, dátum konvertálás stb.)
                : "\nEUR balance:\nError when querying the exchange rate";


        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
