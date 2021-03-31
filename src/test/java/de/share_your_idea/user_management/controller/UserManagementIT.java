package de.share_your_idea.user_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.share_your_idea.user_management.entity.UserEntity;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static de.share_your_idea.user_management.entity.UserRole.ROLE_USER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/* Integration-Test for UserController */
@AutoConfigureMockMvc
@SpringBootTest
@PropertySource("classpath:application.yml")
@PropertySource("classpath:bootstrap.yml")
@Disabled
class UserManagementIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void itShouldSaveNewUserEntity() throws Exception {
        /* Given */
        UserEntity userEntity = new UserEntity(1L, "Michael", "testPassword", ROLE_USER, "testAuthorizationToken");
        /* When */
        /* Perform a PostRequest to the UserController to register a User */
        ResultActions userPostResultActions = mockMvc.perform(post("/api/v1/user-management/register-a-new-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userEntity)));
        /* Perform a GetRequest to the UserController to get a User with an corresponding userId */
        ResultActions userGetResultActions = mockMvc.perform(get("/api/v1/user-management/find-user-by-username/{username}", userEntity.getUsername()));
        /* Then */
        userPostResultActions.andExpect(status().isOk());
        /* Check if User is stored in Repository */
        userGetResultActions
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(userEntity)));
    }
}