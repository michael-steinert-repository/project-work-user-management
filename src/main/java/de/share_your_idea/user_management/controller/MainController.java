package de.share_your_idea.user_management.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.share_your_idea.user_management.entity.UserEntity;
import de.share_your_idea.user_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class MainController {

    @Value("${spring.application.name}")
    private String serviceName;
    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) throws JsonProcessingException {
        log.info("Main Controller: Index Method is called");
        model.addAttribute("serviceName", serviceName);
        log.info("Main Controller: Index Method created ServiceName : {}", new ObjectMapper().writeValueAsString(serviceName));
        return "index";
    }

    @GetMapping(value = {"/userList"})
    public String userList(Model model) throws JsonProcessingException {
        log.info("Main Controller: UserList Method is called");
        List<UserEntity> users = userService.findAllUserEntities();
        model.addAttribute("users", users);
        log.info("Main Controller: Index Method created ServiceName : {}", new ObjectMapper().writeValueAsString(users));
        return "userList";
    }

    @GetMapping(value = {"/addUser"})
    public String showAddUserPage(Model model) {
        UserEntity userEntity = new UserEntity();
        model.addAttribute("userEntity", userEntity);
        return "addUser";
    }

    @PostMapping(value = {"/addUser"})
    public String saveUser(Model model, @ModelAttribute("userEntity") @Valid UserEntity userEntity) throws JsonProcessingException {
        log.info("Main Controller: SaveUser Method is called");
        userService.saveUserEntity(userEntity);
        log.info("Main Controller: SaveUser Method created and saved UserEntity : {}", new ObjectMapper().writeValueAsString(userEntity));
        return "addUser";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public String handleException(Model model, Exception exception) throws JsonProcessingException {
        model.addAttribute("exception", exception);
        log.info("Main Controller: HandleException Method threw Exception : {}", new ObjectMapper().writeValueAsString(exception));
        return "handleException";
    }
}