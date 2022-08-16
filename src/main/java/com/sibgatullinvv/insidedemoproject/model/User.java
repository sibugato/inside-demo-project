package com.sibgatullinvv.insidedemoproject.model;

import javax.persistence.*;
import java.util.Objects;

/*
Автоматически сгенерированный класс POJO для работы с таблицей "users".
Переименовал "login" в "name"
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "login", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String login) {
        this.name = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
