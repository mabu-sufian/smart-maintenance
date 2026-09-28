package com.example.maintenance.entity;

import com.example.maintenance.entity.Enum.Role;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

@Column (nullable = false)
private String name;

    @Column(nullable = false, length = 150)
   private String email;

    @Column(name="hash_passcode", nullable = false)
   private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
   private Role role;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
