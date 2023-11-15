package com.example.jebal.demo.user;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 256)
    private String password;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 100)
    private String phoneNumber;

    @Column(length = 100)
    @Convert(converter = StringArrayConverter.class)
    private List<String> roles = new ArrayList<>();

    @Column(length = 100)
    private String provider;

    @Builder
    public User(int id, String email, String password, String username, String provider, String phoneNumber, List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.provider = provider;
        this.roles = roles;
    }


    public void output(){
        System.out.println(id);
        System.out.println(email);
        System.out.println(password);
        System.out.println(username);
        System.out.println(roles);
    }
}
