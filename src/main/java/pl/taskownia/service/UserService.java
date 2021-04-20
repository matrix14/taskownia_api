package pl.taskownia.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.taskownia.data.UserDataUpdate;
import pl.taskownia.exception.CustomException;
import pl.taskownia.model.Role;
import pl.taskownia.model.User;
import pl.taskownia.model.UserAddress;
import pl.taskownia.model.UserPersonalData;
import pl.taskownia.repository.UserAddressRepository;
import pl.taskownia.repository.UserPersonalDataRepository;
import pl.taskownia.repository.UserRepository;
import pl.taskownia.security.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPersonalDataRepository userPersonalDataRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String login(String uname, String pass) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(uname, pass));
            return jwtTokenProvider.createToken(userRepository.findByUsername(uname).getId(), uname, userRepository.findByUsername((uname)).getRoles());
        } catch (AuthenticationException ex) {
            return "Invalid login or password!";
//            throw new CustomException("Invalid login credentials!", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String register(User u) {
        if(userRepository.findByEmail(u.getEmail())!=null) {
            return "Email taken!";
        }
        if(u.getRoles().contains(Role.ROLE_ADMIN)) {
            return "Cannot register ADMIN!";
        }
        if(!userRepository.existsByUsername(u.getUsername())) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            u.setStatus(User.Status.STATE1);
            u.setCreated_at(new Date(System.currentTimeMillis()));
            u.setUpdated_at(new Date(System.currentTimeMillis()));
            userRepository.save(u);
            return jwtTokenProvider.createToken(u.getId(), u.getUsername(), u.getRoles());
        } else {
            return "Username exist!";
        }
    }

    public String chgPass(HttpServletRequest r, String oldPass, String newPass) {
        if(oldPass.equals(newPass)) {
            return "Old and new pass is same!";
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)), oldPass));
        } catch (AuthenticationException e) {
            return "Invalid old password!";
        }
        User u = this.whoami(r);
        u.setPassword(passwordEncoder.encode(newPass));
        u.setUpdated_at(new Date(System.currentTimeMillis()));
        userRepository.save(u);
        return "Ok";
//        return jwtTokenProvider.createToken(u.getId(), u.getUsername());
    }

    public String chgPassAdmin(HttpServletRequest r, String username, String newPass) {
        User u = userRepository.findByUsername(username);
        u.setPassword(passwordEncoder.encode(newPass));
        u.setUpdated_at(new Date(System.currentTimeMillis()));
        userRepository.save(u);
        return "Ok";
    }

    public void delete(String uname) {
        userRepository.deleteByUsername(uname);
    }

    public User search(String uname) {
        User u = userRepository.findByUsername(uname);

        if(u == null) {
            u = userRepository.findByEmail(uname);
        }
        if(u == null) {
            throw new CustomException("User doesn't exists!", HttpStatus.NOT_FOUND);
        }

        return u;
    }

    public List<User> showAll() {
        return userRepository.findAll();
    }

    public User whoami(HttpServletRequest r) {
        return userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(r)));
    }

    public User updateData(HttpServletRequest request, UserDataUpdate userDataUpdate) {
        User u = userRepository.findByUsername(jwtTokenProvider.getLogin(jwtTokenProvider.resolveToken(request)));
        UserAddress userAddress = u.getAddress();
        UserPersonalData userPersonalData = u.getPersonalData();

        u.setEmail(userDataUpdate.getEmail());

        userPersonalData.setFirstName(userDataUpdate.getFirstName());
        userPersonalData.setLastName(userDataUpdate.getLastName());
        userPersonalData.setPhone(userDataUpdate.getPhone());
        userPersonalData.setBirthDate(userDataUpdate.getBirthDate());

        userAddress.setCity(userDataUpdate.getCity());
        userAddress.setState(userDataUpdate.getState());
        userAddress.setCountry(userDataUpdate.getCountry());
        userAddress.setZipCode(userDataUpdate.getZipCode());

        userRepository.save(u);
        return u;
    }

    public String refresh(String uname) {
        User u = userRepository.findByUsername((uname));
        return jwtTokenProvider.createToken(u.getId(), u.getUsername(), u.getRoles());
    }
}
