package com.sibgatullinvv.insidedemoproject.model;

import javax.persistence.*;
import java.util.Objects;

/*
Автоматически сгенерированный класс POJO для работы с таблицей "messages".
Переименовал "author" в "name"
 */

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "author", nullable = false)
    private String name;

    @Column(name = "message", nullable = false)
    private String message;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String author) {
        this.name = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
