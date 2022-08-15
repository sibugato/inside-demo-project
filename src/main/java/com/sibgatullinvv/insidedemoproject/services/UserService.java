package com.sibgatullinvv.insidedemoproject.services;

import com.sibgatullinvv.insidedemoproject.model.User;
import com.sibgatullinvv.insidedemoproject.repositories.UserRepository;
import com.sibgatullinvv.insidedemoproject.utils.EncodeUtil;
import com.sibgatullinvv.insidedemoproject.utils.JwtTokenUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
Служба связывающая User-контроллер (в данном случае только контроллер аутентификации) и User-репозиторий.
Помимо этого имплементирован интерфейс UserDetailsService
необходимый для получения информации о пользователях (UserDetails), участвующих в процессах аутентификации и генерации JWT токена.
 */

@Service
public class UserService implements UserDetailsService {

    final UserRepository userRepository;
    final JwtTokenUtil jwtTokenUtil;

    public UserService(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    // Метод возвращает коллекцию "User" объектов содержащую все записи из таблицы "users"
    public List<User> usersList() {
        return userRepository.findAll();
    }

    // Метод возвращает пользователя с запрошенным логином из таблицы или бросает исключение об отсутствии оного
    public User getByLogin(String login) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getName().equals(login)) {
                return user;
            }
        }
        throw new UsernameNotFoundException("User not found");
    }

    // Метод проверяет что переданный пароль пользователя соответствует хранящемуся в БД
    public boolean isPasswordCorrect(User testedUser) {
        User originalUser = getByLogin(testedUser.getName());
        return testedUser.getPassword().equals(EncodeUtil.decrypt(originalUser.getPassword()));
    }

    // Метод проверяет что пользователь с переданным логином существует в БД
    public boolean isUserExist(String login) {
        List<User> users = usersList();
        for (User user : users) {
            if (user.getName().equals(login)) {
                return true;
            }
        }
        return false;
    }

    // Метод добавляет нового пользователя в БД
    public void newUser(User user) {
        user.setPassword(EncodeUtil.encrypt(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUser(String login) {
        if (isUserExist(login)) {
            userRepository.delete(getByLogin(login));
        }
    }

    // Метод генерирует JWT токен на основе переданного имени пользователя
    public String generateToken(String username) {
        UserDetails userDetails = loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    // Метод возвращает необходимую информацию в виде "UserDetails" для генерации и аутентификации требуемого пользователя
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByLogin(username);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorityList);
    }
}