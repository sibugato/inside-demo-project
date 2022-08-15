package com.sibgatullinvv.insidedemoproject;

import com.sibgatullinvv.insidedemoproject.model.User;
import com.sibgatullinvv.insidedemoproject.services.MessageService;
import com.sibgatullinvv.insidedemoproject.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InsideDemoProjectApplicationTests {

	@Autowired
	MessageService messageService;
	@Autowired
	UserService userService;

	// Тестирование корректного добавления пользовател в БД
	@Test
	@Order(1)
	void addingUserToDB() {
		User user = new User();
		user.setName("BorisTapochek33");
		user.setPassword("CoolBorisPassword99");
		userService.newUser(user);
		Assertions.assertNotNull(userService.getByLogin(user.getName()));
	}


	// Откат выполненных изменений в БД
	@Test
	@Order(99)
	void cleanUpAfterTests() {
		userService.deleteUser("Boris");
		Assertions.assertEquals(1,1);
	}


}
