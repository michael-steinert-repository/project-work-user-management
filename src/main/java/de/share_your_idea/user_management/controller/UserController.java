package de.share_your_idea.user_management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.share_your_idea.user_management.security.jwt.JwtProvider;
import de.share_your_idea.user_management.entity.UserEntity;
import de.share_your_idea.user_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequestMapping("api/v1/user-management")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register-a-new-user")
    public ResponseEntity<UserEntity> saveNewUserEntity(@RequestBody @Valid UserEntity userEntity) throws JsonProcessingException {
        log.info("User-Controller: RegisterUser-Method is called");
        UserEntity savedUserEntity = userService.saveUserEntity(userEntity);
        log.info("User-Controller: RegisterUser-Method created UserEntity : {}", new ObjectMapper().writeValueAsString(userEntity));
        log.info("User-Controller: RegisterUser-Method saved UserEntity : {}", new ObjectMapper().writeValueAsString(savedUserEntity));
        return new ResponseEntity<>(savedUserEntity, HttpStatus.OK);
    }

    @PostMapping("/update-an-existing-user")
    public ResponseEntity<UserEntity> saveAnExistingUserEntity(@RequestBody @Valid UserEntity userEntity) throws JsonProcessingException {
        log.info("User-Controller: SaveAnExistingUserEntity-Method is called");
        UserEntity savedUserEntity = userService.saveAnExistingUserEntity(userEntity);
        log.info("User-Controller: SaveAnExistingUserEntity-Method created UserEntity : {}", new ObjectMapper().writeValueAsString(userEntity));
        log.info("User-Controller: SaveAnExistingUserEntity-Method saved UserEntity : {}", new ObjectMapper().writeValueAsString(savedUserEntity));
        return new ResponseEntity<>(savedUserEntity, HttpStatus.OK);
    }

    @PostMapping("/update-all-existing-users")
    public ResponseEntity<List<UserEntity>> saveAllExistingUserEntities(@RequestBody List<UserEntity> userEntityList) throws JsonProcessingException {
        log.info("User-Controller: SaveAllExistingUserEntities-Method is called");
        List<UserEntity> savedUserEntityList = userService.saveAllExistingUserEntities(userEntityList);
        log.info("User-Controller: SaveAllExistingUserEntities-Method saved UserEntityList : {}", new ObjectMapper().writeValueAsString(savedUserEntityList));
        return new ResponseEntity<>(savedUserEntityList, HttpStatus.OK);
    }

    @PostMapping("/authenticate-an-existing-user")
    public ResponseEntity<UserEntity> authenticateUserEntity(@RequestBody UserEntity userEntity) throws JsonProcessingException {
        log.info("User-Controller: Authentication-Method is called");
        UserEntity foundUserEntity = userService.findUserEntityByUsernameAndPassword(userEntity.getUsername(), userEntity.getPassword());
        String token = jwtProvider.generateToken(foundUserEntity.getUsername());
        foundUserEntity.setAuthorizationToken(token);
        userService.saveUserEntity(foundUserEntity);
        log.info("User-Controller: Authentication-Method created UserEntity : {}", new ObjectMapper().writeValueAsString(foundUserEntity));
        return new ResponseEntity<>(foundUserEntity, HttpStatus.OK);
    }

    @GetMapping(path = "/find-user-by-username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntity> findUserEntityByUsername(@PathVariable("username") String username) throws JsonProcessingException {
        log.info("User-Controller: FindUserByUsername-Method is called");
        UserEntity userEntity = userService.findUserEntityByUsername(username);
        log.info("User-Controller: FindUserByUsername-Method created UserEntity : {}", new ObjectMapper().writeValueAsString(userEntity));
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }

    @GetMapping(path = {"/find-all-users"})
    public ResponseEntity<List<UserEntity>> findAllUserEntities() throws JsonProcessingException {
        log.info("User-Controller: FindAllUserEntities-Method is called");
        List<UserEntity> userEntityList = userService.findAllUserEntities();
        log.info("User-Controller: FindAllUserEntities-Method created UserEntityList : {}", new ObjectMapper().writeValueAsString(userEntityList));
        return new ResponseEntity<>(userEntityList, HttpStatus.OK);
    }
}