package com.nor2code.springboot.thymeleafdemo.entity;

import jakarta.persistence.*;

@Entity //egy annotáció a Spring Boot + JPA (Java Persistence API) világában, amely azt jelenti, hogy az adott Java osztály egy adatbázisbeli tábla reprezentációja lesz.
@Table(name = "user")
public class User {

    // define fields
    @Id
    //@Column(name="user_id")
    private String userId;

    @Column(name="amount")
    private int amount;

    @OneToOne
    @MapsId //@MapsId azt jelenti, hogy a User entitás azonosítója ugyanaz, mint a hozzá tartozó Member-é (1:1 azonos kulcsú kapcsolat)
    @JoinColumn(name = "user_id")
    private Members members;


    // define constructors
    public User() {

    }

    public User(String userId, int amount) {
        this.userId = userId;
        this.amount = amount;
    }

    // define getter/setter

    public String getUser_id() {
        return userId;
    }

    public void setUser_id(String user_id) {
        this.userId = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + userId +
                ", amount=" + amount +
                '}';
    }
}
