package com.sibgatullinvv.insidedemoproject.controller;

import com.sibgatullinvv.insidedemoproject.model.Message;
import com.sibgatullinvv.insidedemoproject.services.MessageService;
import com.sibgatullinvv.insidedemoproject.services.UserService;
import com.sibgatullinvv.insidedemoproject.utils.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/*
Этот контроллер будет заниматься сообщениями пользователей, их созданием и предоставлением по запросу.
 */

@RestController
@RequestMapping("/message")
public class MessagesController {

    private MessageService messageService;
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;


    public MessagesController(MessageService messageService, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.messageService = messageService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /*
    POST endpoint для отправки/получения сообщений. В запросе ожидаются данные "name" и "message" в формате JSON.
    В заголовке "Authorization" ожидается токен. Сначала в фильтре запросов идёт проверка валидности самого токена,
    а затем в данном эндпоинте идут описанные ниже процедуры.
     */

    // P.s. мы тут к божественному классу приближаемся, до конца дня исправлю (16.08.2022)
    @PostMapping()
    public ResponseEntity<?> writeMessage(@RequestBody Message requestMessage, HttpServletRequest request) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        // Извлекаем имя пользователя из переданного токена (первичная проверка токена идёт в фильтре запросов)
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization").substring(7));

        // Проверка что параметр "name" передан в запросе и он не пустой
        if (requestMessage.getName() == null || requestMessage.getName().equals("")) {
            responseMap.put("error", "«name» field empty or missing");
            return ResponseEntity.status(406).body(responseMap);
        }

        // Проверка что параметр "message" передан в запросе и он не пустой
        else if (requestMessage.getMessage() == null || requestMessage.getMessage().equals("")) {
            responseMap.put("error", "«message» field empty or missing");
            return ResponseEntity.status(406).body(responseMap);
        }

        // Проверка что переданное имя соответствует существующему в БД пользователю
        else if (!userService.isUserExist(requestMessage.getName())) {
            responseMap.put("error", "there is no such user");
            return ResponseEntity.status(401).body(responseMap);
        }

        // Проверка что переданный в запросе токен принадлежит указанному пользователю
        else if (!username.equals(requestMessage.getName())) {
            responseMap.put("error", "token does not belong to the user");
            return ResponseEntity.status(403).body(responseMap);
        }

        // Небольшая подготовка к проверке типа запроса (требуется запись сообщения в БД или показ сообщений из БД)
        int messageQuantity = 0;
        boolean isHistoryRequest = true;
        String message = requestMessage.getMessage();
        // Запрос на запись должен иметь вид "History %d", поэтому сначала разбиваем запрос через пробелы
        String[] checkMessage = message.split(" ");

        // Если получилось 2 части и первая часть это "history", то пробуем запарсить в Integer вторую
        if (checkMessage.length == 2 && checkMessage[0].equals("history")) {
            try {
                messageQuantity = Integer.parseInt(checkMessage[1]);
            }
            catch (NumberFormatException e) {
                isHistoryRequest = false;
            }

            // Если запарсить не получилось (см. "catch" блок выше) - то этот "if" блок пропускается и сразу идёт запись сообщения в БД
            // Если запарсить получилось - значит это запрос на показ истории сообщений и продолжается выполнение по пунктам
            if (isHistoryRequest) {

                // Если запрошено менее одного сообщения, то в ответ будет отправлено соответствующее предупреждение
                if (messageQuantity < 1) {
                    responseMap.put("warning", "there, at zero and below, is nothing to show...");
                    return ResponseEntity.status(200).body(responseMap);
                }

                // Если в БД сейчас ни одного сообщения, то в ответ будет отправлено соответствующее уведомление
                else if (messageService.showAll().size() == 0) {
                    responseMap.put("status", "there no messages for now, be first!");
                    return ResponseEntity.status(200).body(responseMap);
                }

                // В остальных случаях запрошенное (или максимально доступное) количество сообщений из БД вернётся в ответ в формате JSON.
                String template = "%d. %s";
                long messagesCount = 0;
                for (Message m : messageService.showHistory(messageQuantity)) {
                    responseMap.put(String.format(template, ++messagesCount, m.getName()), m.getMessage());
                }
                return ResponseEntity.ok(responseMap);
            }
        }

        // Если же это не был запрос истории сообщений - то здесь сообщение будет записано в БД
        messageService.saveMessage(requestMessage);
        responseMap.put("status", "message saved successfully");
        return ResponseEntity.status(201).body(responseMap);
    }
}

