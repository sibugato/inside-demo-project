package com.sibgatullinvv.insidedemoproject.model;

import javax.persistence.*;
import java.util.Objects;

/*
Автоматически сгенерированный класс POJO для работы с таблицей "messages".
Переименовал "author" в "name"
 */


@Entity
@Table(name = "messages", schema = "public", catalog = "insideDB")
public class Message {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "author", nullable = false, length = 32)
    private String name;
    @Basic
    @Column(name = "message", nullable = false, length = -1)
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(id, message1.id) && Objects.equals(name, message1.name) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, message);
    }
}
