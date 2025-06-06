package com.nor2code.springboot.thymeleafdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles" , // ez köti az osztályt a "roles" táblához
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role"}))
public class Roles {

    @Id
    private String userId; // Technikai primer kulcs

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId //ua. az Id, mint a members tábla id-je
    @JoinColumn(name = "user_id", nullable = false) // Ha az SQL definícióban NOT NULL, akkor itt is érdemes nullable = false, Validáció: JPA még az előtt hibát dobhat, hogy leküldenéd az adatot az adatbázisba.
    private Members members;

    public Roles () {

    }

    public Roles(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }
}
