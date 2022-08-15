package com.sibgatullinvv.insidedemoproject.model;

import javax.persistence.*;
import java.util.Objects;

/*
Автоматически сгенерированный класс POJO для работы с таблицей "users".
Переименовал "login" в "name"
 */

@Entity
@Table(name = "users", schema = "public", catalog = "insideDB")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "login", nullable = false, length = 32)
    private String name;
    @Basic
    @Column(name = "password", nullable = false, length = 32)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
