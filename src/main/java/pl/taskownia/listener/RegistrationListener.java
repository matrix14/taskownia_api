package pl.taskownia.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.taskownia.event.OnRegistrationEvent;
import pl.taskownia.model.User;
import pl.taskownia.service.UserService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationEvent> {
    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnRegistrationEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createConfirmationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Potwierdzenie rejestracji";
        String confirmationUrl = "/user/registration-confirm?token=" + token;
        String message = "Pomyślnie zarejestrowano, aby potwierdzić rejestracje, kliknij w link poniżej:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl); //FIXME address of website and backend
        javaMailSender.send(email);
    }
}
