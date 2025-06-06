package com.nor2code.springboot.thymeleafdemo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "withdrawals")
public class Withdrawals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id; // Egyedi azonosító minden tranzakcióhoz

    @Column(name = "user_id", insertable=false, updatable=false)
    private String userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "created_at", updatable = false) //az értéket nem lehet utólag frissíteni
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Ha az SQL definícióban NOT NULL, akkor itt is érdemes nullable = false, Validáció: JPA még az előtt hibát dobhat, hogy leküldenéd az adatot az adatbázisba.
    private Members members;

    public Withdrawals() {}

    public Withdrawals(String userId, int amount, LocalDateTime createdAt) {
        this.userId = userId;
        this.amount = amount;
        //this.createdAt = createdAt; nem kell, az automatikusan be lesz állítva
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }
}
