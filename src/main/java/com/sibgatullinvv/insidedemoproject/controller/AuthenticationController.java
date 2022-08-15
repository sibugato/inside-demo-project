package com.sibgatullinvv.insidedemoproject.controller;

import com.sibgatullinvv.insidedemoproject.model.User;
import com.sibgatullinvv.insidedemoproject.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/*
Этот контроллер будет заниматься авторизацией и регистрацией пользователей.
В обоих случаях будут генерироваться и отправляться в ответе JWT токены.
 */

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    final AuthenticationManager authenticationManager;
    final UserService userService;

    public AuthenticationController( AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    /*
    POST endpoint для авторизации пользователя. В запросе ожидаются данные "name" и "password" в формате JSON.
    При наличии корректных данных начинаются описанные в методе проверки.
    Если пользователь существует и предоставленный пароль подходит, то в ответе отправляется JWT токен.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        HashMap<String, Object> responseMap = new LinkedHashMap<>();

        // Проверка что параметр "name" передан в запросе и он не пустой
        if (user.getName() == null || user.getName().equals("")) {
            responseMap.put("error", "«name» field empty or missing");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что параметр "password" передан в запросе и он не пустой
        else if (user.getPassword() == null || user.getPassword().equals("")) {
            responseMap.put("error", "«password» field empty or missing");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что такой пользователь существует в БД
        else if (!userService.isUserExist(user.getName())) {
            responseMap.put("error", "invalid credentials");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что переданный пароль подходит
        else if (!userService.isPasswordCorrect(user)) {
            responseMap.put("error", "invalid credentials");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Генерируем и передаём токен в ответе
        responseMap.put("status", "successful authorization");
        responseMap.put("user", user.getName());
        responseMap.put("token", userService.generateToken(user.getName()));
        return ResponseEntity.ok(responseMap);
    }

    /*
    POST endpoint для регистрации пользователя. В запросе ожидаются данные "name" и "password" в формате JSON.
    При наличии корректных данных создаётся новый пользователь в таблице users и в ответе выдаётся JWT токен.
     */
    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        Map<String, Object> responseMap = new LinkedHashMap<>();

        // Проверка что параметр "name" передан в запросе и он не пустой
        if (user.getName() == null || user.getName().equals("")) {
            responseMap.put("error", "«name» field empty or missing");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что параметр "password" передан в запросе и он не пустой
        else if (user.getPassword() == null || user.getPassword().equals("")) {
            responseMap.put("error", "«password» field empty or missing");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Этот логин зарезервирован для тестирования
        else if (user.getName().equals("BorisTapochek33") && !user.getPassword().equals("CoolBorisPassword99")) {
            responseMap.put("error", "user already exist");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что такого пользователя ещё не существует в БД
        else if (userService.isUserExist(user.getName())) {
            responseMap.put("error", "user already exist");
            return ResponseEntity.status(401).body(responseMap);
        }

        // На случай передачи в запросе поля "id" тут его удаляем.
        user.setId(null);
        // Добавляем нового пользователя в БД
        userService.newUser(user);
        // Генерируем и передаём токен в ответе
        responseMap.put("status", "account created successfully");
        responseMap.put("user", user.getName());
        responseMap.put("token", userService.generateToken(user.getName()));
        return ResponseEntity.ok(responseMap);
    }
}