package pl.taskownia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.taskownia.model.Chat;
import pl.taskownia.model.User;
import pl.taskownia.service.ChatService;
import pl.taskownia.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/get-last")
    public Page<Chat> getLastChats(@RequestParam(required = false) Integer howMany) throws JsonProcessingException {
        if(howMany==null||howMany==0) { howMany = 10; }
        return chatService.getLastChat(howMany);
    }

    @PostMapping("/sent")
    public String sentMessage(HttpServletRequest r, @RequestParam String msg) {
        return chatService.sent(r,msg);
    }
}
