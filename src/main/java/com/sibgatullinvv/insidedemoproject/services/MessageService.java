package com.sibgatullinvv.insidedemoproject.services;

import com.sibgatullinvv.insidedemoproject.model.Message;
import com.sibgatullinvv.insidedemoproject.dto.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
Служба связывающая Message-контроллер и Message-репозиторий
 */

@Service
public class MessageService {

    final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // Метод сохраняет в таблицу "messages" переданный POJO объект "Message"
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    // Метод возвращает коллекцию "Message" объектов из таблицы "messages" в запрошенном кол-ве, начиная с самых новых.
    public List<Message> showHistory(int amount) {
        List<Message> allMessages = messageRepository.findAll();
        List<Message> requestedMessages = new ArrayList<>();
        int size = allMessages.size();
        while (amount > 0) {
            if (size == 0) {
                break;
            }
            requestedMessages.add(allMessages.get(--size));
            amount--;
        }
        return requestedMessages;
    }

    // Метод возвращает коллекцию "Message" объектов содержащую все записи из таблицы "messages"
    public List<Message> showAll(){
        return messageRepository.findAll();
    }
}
