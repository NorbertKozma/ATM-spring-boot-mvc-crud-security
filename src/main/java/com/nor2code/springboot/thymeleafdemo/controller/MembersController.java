package com.nor2code.springboot.thymeleafdemo.controller;

import com.nor2code.springboot.thymeleafdemo.dto.NewMemberRequest;
import com.nor2code.springboot.thymeleafdemo.entity.User;
import com.nor2code.springboot.thymeleafdemo.service.MembersService;
import com.nor2code.springboot.thymeleafdemo.service.UserService;
import com.nor2code.springboot.thymeleafdemo.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class MembersController {

    private MembersService membersService;
    private UserService userService;

    @Autowired
    public MembersController(MembersService theMembersService, UserService theUserService) {
        membersService = theMembersService;
        userService = theUserService;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerNewMember(@RequestBody NewMemberRequest request) { // A request objektumot a Spring automatikusan feltölti a JSON body alapján.Ezért van szükség getterekre (getUserId(), getPlainPassword() stb.), hogy lekérd a kliens által küldött adatokat.

        try {
            userService.createNewMember(
                    request.getUserId(),
                    request.getPlainPassword(),
                    request.getInitialAmount(),
                    request.getRoles()
            );
            return ResponseEntity.ok("Successful registration: \n\n" + FormatUtils.formatAccountNumber(request.getUserId()));
        } catch (DataIntegrityViolationException ex) {
            // Ha az adatbázis megsérti az egyediség szabályt (pl. PRIMARY kulcs duplikált)
            if (ex.getMessage() != null && ex.getMessage().contains("Duplicate entry")) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("This account number already exists. Please choose another one.");
            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Database error: " + ex.getMessage());
            }
        } catch (Exception e) {
            // Minden más hiba ide esik, pl. null pointer, hirtelen leállás stb.
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: " + e.getMessage());
        }

    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam("accountNumber") String theId, Principal principal) {

        try {

            // Ne engedje, hogy az admin saját magát törölje
            if (theId.equals(principal.getName())) { // így hasonlítunk két String-et Java-ban
                return new ResponseEntity<>("Cannot delete your own admin account.", HttpStatus.BAD_REQUEST);
            }

            User theUser = userService.findById(theId);

            // delete the employee
            membersService.deleteById(theId);
            return new ResponseEntity<>("Successfully deleted account with id:\n" + FormatUtils.formatAccountNumber(theId), HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>("Unexpected error\n\n" + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
