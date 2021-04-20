package pl.taskownia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskownia.exception.CustomException;
import pl.taskownia.model.Chat;
import pl.taskownia.model.User;
import pl.taskownia.repository.ChatRepository;
import pl.taskownia.repository.UserRepository;
import pl.taskownia.security.JwtTokenProvider;
import pl.taskownia.serializer.ChatSerializer;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String sent(HttpServletRequest r, String msg) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));

        Chat chat = new Chat();
        chat.setUser(u);
        chat.setMessage(msg);
        chat.setDate(new Date(System.currentTimeMillis()));
        chatRepository.save(chat);
        return "Ok";
    }

    public Page<Chat> getLastChat(Integer howMany) {
        Page<Chat> chatPage = chatRepository.findAll(
                PageRequest.of(0, howMany, Sort.by(Sort.Direction.DESC, "id")));
        return chatPage;
    }
}
