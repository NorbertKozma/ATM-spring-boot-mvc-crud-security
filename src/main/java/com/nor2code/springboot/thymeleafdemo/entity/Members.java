package com.nor2code.springboot.thymeleafdemo.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Members {

    @Id
    @Column(name = "user_id")
    private String user_id;

    @Column(name = "pw", length = 68, nullable = false, unique = true)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    // 1:N kapcsolat a Role entitással
    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true) //A Role entitás members mezője a tulajdonosa a kapcsolatnak (ott van az @ManyToOne és @JoinColumn). ,orphanRemoval: Ha egy Role eltávolításra kerül a roles listából, és máshova nem tartozik, akkor az adatbázisból is törlődik.
    private List<Roles> role = new ArrayList<>();

    // 1:1 kapcsolat a User entitással
    @OneToOne(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    // 1:N kapcsolat a Withdrawals entitással
    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true) //A Withdrawals entitás members mezője a tulajdonosa a kapcsolatnak (ott van az @ManyToOne és @JoinColumn). ,orphanRemoval: Ha egy Role eltávolításra kerül a roles listából, és máshova nem tartozik, akkor az adatbázisból is törlődik.
    private List<Withdrawals> withdrawals = new ArrayList<>();

    public Members () {
    }

    public Members(String user_id, String password, boolean active, List<Roles> roles) {
        this.user_id = user_id;
        this.password = password;
        this.active = active;
        this.role = roles;
        //this.withdrawals = withdrawals;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Roles> getRoles() {
        return role;
    }

    public void setRoles(List<Roles> roles) {
        this.role = roles;
    }

    public List<Withdrawals> getWithdrawals() {
        return withdrawals;
    }

    public void setWithdrawals(List<Withdrawals> withdrawals) {
        this.withdrawals = withdrawals;
    }



    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
