package org.example.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {
    protected Long userId;
    protected  String firstName;
    protected String lastName;
    protected String username;
    protected String password;
    protected boolean isActive;
}
