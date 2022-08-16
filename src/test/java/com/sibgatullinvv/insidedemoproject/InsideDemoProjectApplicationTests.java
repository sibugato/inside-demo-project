package com.sibgatullinvv.insidedemoproject;

import com.sibgatullinvv.insidedemoproject.model.User;
import com.sibgatullinvv.insidedemoproject.services.MessageService;
import com.sibgatullinvv.insidedemoproject.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/*
В ближайшее время фуекционал тестирования будет значительно расширен. Пока познакомьтесь с Борисом.
 */

@SpringBootTest
class InsideDemoProjectApplicationTests {

	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;
	private User boris;

	{
		boris = new User();
		boris.setName( "BorisTapochek33");
		boris.setPassword("CoolBorisPassword99");
	}

	// добавления пользовател в БД
	@Test
	@Order(1)
	void addingUserToDB() {
		User user = new User();
		user.setName("BorisTapochek33");
		user.setPassword("CoolBorisPassword99");
		userService.newUser(user);
		Assertions.assertNotNull(userService.getByLogin(user.getName()));
	}


	// удаление пользователя из БД
	@Test
	@Order(2)
	void deleteUserFromDB() {
		userService.deleteUser(boris.getName());
		Assertions.assertFalse(userService.isUserExist(boris.getName()));
	}
}
