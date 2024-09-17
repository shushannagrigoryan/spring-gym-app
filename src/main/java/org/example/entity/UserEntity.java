package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;
    @Column(name = "first_name", nullable = false)
    protected String firstName;
    @Column(name = "last_name", nullable = false)
    protected String lastName;
    @Column(name = "username", nullable = false, unique = true)
    protected String username;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "is_active", nullable = false)
    protected boolean isActive;
}
