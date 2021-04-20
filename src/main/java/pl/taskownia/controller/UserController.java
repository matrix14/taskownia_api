package pl.taskownia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.taskownia.data.UserDataUpdate;
import pl.taskownia.model.User;
import pl.taskownia.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/change-pwd")
    public String chgPwd(HttpServletRequest r, @RequestParam String oldPass,
                         @RequestParam String newPass) {
        return userService.chgPass(r, oldPass, newPass);
    }

    @PostMapping("/change-pwd-admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String chgPwdAdm(HttpServletRequest r, @RequestParam String username,
                         @RequestParam String newPass) {
        return userService.chgPassAdmin(r, username, newPass);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')") //TODO: uncomment, but not working properly with ADMIN
    public List<User> showAll() {
        return userService.showAll();
    }

    @GetMapping("/me")
    public User whoami(HttpServletRequest r) {
        return userService.whoami(r);
    }

    /*
    request.data[]
    name, email, first_name, last_name, phone, birth_date, city, state, country, zip_code

    personal_datas: first_name, last_name, phone, birth_date (+updated_at)
    image: image_path
    addresses: city, country, zip_code, state

     */
    //TODO: change personal image here or other request? For me other request
    @PostMapping("/update-data")
    public User updateData(HttpServletRequest r, @RequestBody UserDataUpdate userDataUpdate) {
        return userService.updateData(r, userDataUpdate); //FIXME zrobic
    }
}
