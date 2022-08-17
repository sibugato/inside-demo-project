package com.sibgatullinvv.insidedemoproject;

import com.sibgatullinvv.insidedemoproject.controller.AuthenticationController;
import com.sibgatullinvv.insidedemoproject.model.User;
import com.sibgatullinvv.insidedemoproject.services.MessageService;
import com.sibgatullinvv.insidedemoproject.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;


@SpringBootTest
class InsideDemoProjectApplicationTests {

	@Autowired
	private MessageService messageService;
	@Autowired
    private UserService userService;
    @Autowired
    private AuthenticationController authenticationController;
    private User boris;

    @BeforeEach
    public void setBoris(){
        boris = new User();
        boris.setName("BorisTapochek33");
        boris.setPassword("CoolBorisPassword99");
    }

    @Test
    void logInWithEmptyLogin() {
        boris.setName("");
        ResponseEntity responseEntity = authenticationController.loginUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals( "«name» field empty or missing}",response[1]);
    }

    @Test
    void logInWithoutLogin() {
        boris.setName(null);
        ResponseEntity responseEntity = authenticationController.loginUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals( "«name» field empty or missing}", response[1]);
    }

    @Test
    void registerWithTooLongUsername() {
        boris.setName("AbsolutelyAccurate33CharacterName");
        ResponseEntity responseEntity = authenticationController.saveUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals( "maximum username length is 32 characters}",response[1]);
    }

    @Test
    void logInWithEmptyPassword() {
        boris.setPassword("");
        ResponseEntity responseEntity = authenticationController.loginUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals( "«password» field empty or missing}", response[1]);
    }

    @Test
    void logInWithoutPassword() {
        boris.setPassword(null);
        ResponseEntity responseEntity = authenticationController.loginUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals( "«password» field empty or missing}", response[1]);
    }

    @Test
    void logInWithNotExistingUser() {
        boris.setName("justRandomStrangerFromAbove");
        boris.setPassword("LjhfoiuHBN76t87t8%Tr76gikF&*TPES");
        ResponseEntity responseEntity = authenticationController.loginUser(boris);
        String[] response = responseEntity.getBody().toString().split("=");
        Assertions.assertEquals("invalid credentials}",response[1]);
    }
    @Test
    void lsUserExisMethodTest() {
        Assertions.assertFalse(userService.isUserExist(boris.getName()));
    }
}
