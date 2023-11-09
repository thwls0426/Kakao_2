package com.example.jebal.demo.user;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column
    private String nickname;

    @Column
    private String picture;


    @Column(length = 256, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String username;

    @Column(length = 11, nullable = false)
    private String phoneNumber;

    @Column(length = 30, nullable = false)
    @Convert(converter = StringArrayConverter.class)
    @Builder.Default
    private List<String> role = new ArrayList<>();

    @Builder
    public User(int id, String email, String password, String username, String phoneNumber, List<String> role, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.nickname = nickname;
    }

    public User update(String name, String picture){
        this.username = name;
        this.picture = picture;

        return this;
    }

    public void output(){
        System.out.println(id);
        System.out.println(email);
        System.out.println(password);
        System.out.println(username);
        System.out.println(role);
    }
}
