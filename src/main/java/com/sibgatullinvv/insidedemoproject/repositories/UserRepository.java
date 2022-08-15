package com.sibgatullinvv.insidedemoproject.repositories;

import com.sibgatullinvv.insidedemoproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
Репозиторий непосредственно взаимодействующий с таблицей "users"
 */

public interface UserRepository extends JpaRepository<User, Long> {
}
