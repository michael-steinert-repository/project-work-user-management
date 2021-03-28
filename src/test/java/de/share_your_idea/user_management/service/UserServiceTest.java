package de.share_your_idea.user_management.service;

import de.share_your_idea.user_management.entity.UserEntity;
import de.share_your_idea.user_management.exception.CustomNotFoundException;
import de.share_your_idea.user_management.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static de.share_your_idea.user_management.entity.UserRole.ROLE_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/* Unit-Test for UserService */
class UserServiceTest {
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<UserEntity>> userEntityListArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userEntityRepository, passwordEncoder);
    }

    @Test
    void itShouldSaveUserEntity() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", "testPassword", ROLE_USER, "testAuthorizationToken");
        /* Mocking the Return if Method save() is called */
        given(userEntityRepository.save(any(UserEntity.class))).willReturn(any(UserEntity.class));
        /* When */
        userService.saveUserEntity(userEntity);
        /* Then */
        /* The Method verify() verifies that the Method save() is invoked in the Repository */
        /* The Method capture() captures the Value in the Method save() */
        verify(userEntityRepository).save(userEntityArgumentCaptor.capture());
        UserEntity userEntityFromService = userEntityArgumentCaptor.getValue();
        assertThat(userEntityFromService.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(userEntityFromService.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(userEntityFromService.getPassword()).isEqualTo(passwordEncoder.encode(userEntity.getPassword()));
        assertThat(userEntityFromService.getUserRole()).isEqualTo(userEntity.getUserRole());
    }

    @Test
    void itShouldNotSaveAnExistingUserEntityWhenIsNotPresent() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", "testPassword", ROLE_USER, "testAuthorizationToken");
        /* When */
        /* Then */
        assertThatThrownBy(() -> userService.saveAnExistingUserEntity(userEntity))
                .isInstanceOf(CustomNotFoundException.class)
                .hasMessageContaining(String.format("UserEntity with Username like %s not found.", userEntity.getUsername()));
    }

    @Test
    void itShouldSaveAllExistingUserEntities() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", "testPassword", ROLE_USER, "testAuthorizationToken");
        List<UserEntity> userEntityList = List.of(userEntity);
        /* Mocking the Return if Method saveAll() is called */
        given(userEntityRepository.saveAll(any(List.class))).willReturn(any(List.class));
        /* When */
        userService.saveAllExistingUserEntities(userEntityList);
        /* Then */
        /* The Method verify() verifies that the Method saveAll() is invoked in the Repository */
        /* The Method capture() captures the Value in the Method saveAll() */
        verify(userEntityRepository).saveAll(userEntityListArgumentCaptor.capture());
        List<UserEntity> userEntityListFromService = userEntityListArgumentCaptor.getValue();
        assertThat(userEntityListFromService).isEqualTo(userEntityList);
    }

    @Test
    void itShouldFindUserEntityByUsername() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", passwordEncoder.encode("testPassword"), ROLE_USER, "testAuthorizationToken");
        /* Mocking the Return if Method findUserEntityByUsername() is called */
        given(userEntityRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.of(userEntity));
        /* When */
        UserEntity userEntityFromService = userService.findUserEntityByUsername(userEntity.getUsername());
        /* Then */
        /* The Method verify() verifies that the Method findUserEntityByUsername() is invoked in the Repository */
        /* The Method capture() captures the Value in the Method findUserEntityByUsername() */
        verify(userEntityRepository).findUserEntityByUsername(stringArgumentCaptor.capture());
        String usernameArgumentCaptorValue = stringArgumentCaptor.getValue();
        assertThat(usernameArgumentCaptorValue).isEqualTo(userEntity.getUsername());
        assertThat(userEntityFromService.getUserId()).isEqualTo(userEntity.getUserId());
        assertThat(userEntityFromService.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(userEntityFromService.getPassword()).isEqualTo(passwordEncoder.encode(userEntity.getPassword()));
        assertThat(userEntityFromService.getUserRole()).isEqualTo(userEntity.getUserRole());
    }

    @Test
    void itShouldFindUserEntityByUsernameAndPassword() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", passwordEncoder.encode("testPassword"), ROLE_USER, "testAuthorizationToken");
        /* Mocking the Return if Method findUserEntityByUsername() is called */
        given(userEntityRepository.findUserEntityByUsername(any(String.class))).willReturn(Optional.of(userEntity));
        /* When */
        /* Then */
        assertThatThrownBy(() -> userService.findUserEntityByUsernameAndPassword(userEntity.getUsername(), userEntity.getPassword()))
                .isInstanceOf(CustomNotFoundException.class)
                .hasMessageContaining(String.format("UserEntity with Username like %s and Password %s not found.", userEntity.getUsername(), userEntity.getPassword()));
    }

    @Test
    void itShouldFindAllUserEntities() {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", "testPassword", ROLE_USER, "testAuthorizationToken");
        List<UserEntity> userEntityList = List.of(userEntity);
        /* Mocking the Return if Method saveAll() is called */
        given(userEntityRepository.findAll()).willReturn(userEntityList);
        /* When */
        List<UserEntity> userEntityListFromService = userService.findAllUserEntities();
        /* Then */
        assertThat(userEntityListFromService).isInstanceOf(List.class);
    }
}