package de.share_your_idea.user_management.service;

import de.share_your_idea.user_management.entity.UserEntity;
import de.share_your_idea.user_management.entity.UserRole;
import de.share_your_idea.user_management.exception.CustomEmptyInputException;
import de.share_your_idea.user_management.exception.CustomNotFoundException;
import de.share_your_idea.user_management.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserEntityRepository userEntityRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity saveUserEntity(UserEntity userEntity) {
        log.info("User Service: SaveUserEntity Method is called");
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setUserRole(UserRole.ROLE_USER);
        return userEntityRepository.save(userEntity);
    }

    public UserEntity saveAnExistingUserEntity(UserEntity userEntity) {
        log.info("User Service: SaveAnExistingUserEntity Method is called");
        Optional<UserEntity> userEntityOptional = userEntityRepository.findUserEntityByUsername(userEntity.getUsername());
        if(userEntityOptional.isPresent()) {
            /* Using the existing ID to save UserEntity */
            UserEntity userEntityFromRepository = userEntityOptional.get();
            userEntityFromRepository.setUsername(userEntity.getUsername());
            userEntityFromRepository.setPassword(userEntity.getPassword());
            userEntityFromRepository.setAuthorizationToken(userEntity.getAuthorizationToken());
            userEntityFromRepository.setUserRole(userEntity.getUserRole());
            return userEntityRepository.save(userEntityFromRepository);
        }
        throw new CustomNotFoundException(String.format("UserEntity with Username like %s not found.", userEntity.getUsername()));
    }

    public List<UserEntity> saveAllExistingUserEntities(List<UserEntity> userEntityList) {
        log.info("User Service: SaveAllExistingUserEntities Method is called");
        if(!userEntityList.isEmpty()) {
            List<UserEntity> userEntityListFromRepository = new ArrayList<>();
            /* Using the existing ID to save UserEntity */
            for(UserEntity userEntity : userEntityList) {
                Optional<UserEntity> userEntityOptional = userEntityRepository.findUserEntityByUsername(userEntity.getUsername());
                if(userEntityOptional.isPresent()) {
                    UserEntity userEntityFromRepository = userEntityOptional.get();
                    userEntityFromRepository.setUsername(userEntity.getUsername());
                    userEntityFromRepository.setPassword(userEntity.getPassword());
                    userEntityFromRepository.setAuthorizationToken(userEntity.getAuthorizationToken());
                    userEntityFromRepository.setUserRole(userEntity.getUserRole());
                    userEntityListFromRepository.add(userEntityFromRepository);
                } else {
                    userEntityListFromRepository.add(userEntity);
                }
            }
            return userEntityRepository.saveAll(userEntityListFromRepository);
        }
        throw new CustomNotFoundException("UserEntityList was empty.");
    }

    public UserEntity findUserEntityByUsername(String username) {
        log.info("User Service: FindUserEntityByUsername Method is called");
        if (username != null) {
            Optional<UserEntity> userEntityOptional = userEntityRepository.findUserEntityByUsername(username);
            return userEntityOptional.orElseThrow(()-> new CustomNotFoundException(String.format("UserEntity with Username like %s not found.", username)));
        }
        throw new CustomEmptyInputException("The Username is empty.");
    }

    public UserEntity findUserEntityByUsernameAndPassword(String username, String password) {
        log.info("User Service: FindUserEntityByUsernameAndPassword Method is called");
        UserEntity userEntity = findUserEntityByUsername(username);
        if (userEntity != null) {
            if (passwordEncoder.matches(password, userEntity.getPassword())) {
                return userEntity;
            }
        }
        throw new CustomNotFoundException(String.format("UserEntity with Username like %s and Password %s not found.", username, password));
    }

    public List<UserEntity> findAllUserEntities() {
        log.info("User Service: FindAllUserEntities Method is called");
        return userEntityRepository.findAll();
    }
}
