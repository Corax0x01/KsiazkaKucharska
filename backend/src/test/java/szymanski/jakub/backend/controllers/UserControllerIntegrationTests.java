package szymanski.jakub.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import szymanski.jakub.backend.TestDataUtil;
import szymanski.jakub.backend.domain.entities.UserEntity;
import szymanski.jakub.backend.services.UserService;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatGetUsersReturnsStatus200AndAllUsers() throws Exception {
        UserEntity testUserA = TestDataUtil.createTestUserA();
        UserEntity testUserB = TestDataUtil.createTestUserB();
        UserEntity savedUserA = userService.save(testUserA);
        UserEntity savedUserB = userService.save(testUserB);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].username").value(savedUserA.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[0].email").value(savedUserA.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].username").value(savedUserB.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.[1].email").value(savedUserB.getEmail())
        );
    }

    @Test
    public void testThatGetExistingUserReturnsStatus200AndUserWithGivenId() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();

        UserEntity savedUser = userService.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(savedUser.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedUser.getEmail())
        );
    }

    @Test
    public void testThatGetNonExistingUserReturnsStatus404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/123")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatCreateUserReturnsStatus201AndSavedUser() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        testUser.setId(null);
        String userJson = objectMapper.writeValueAsString(testUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isNumber()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(testUser.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(testUser.getEmail())
        );
    }

    @Test
    public void testThatFullUpdateUserReturnsStatus200AndUpdatedUserIfUserExists() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        String newUsername = "Changed username";

        UserEntity savedUser = userService.save(testUser);

        savedUser.setUsername(newUsername);

        String userJson = objectMapper.writeValueAsString(savedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(newUsername)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedUser.getEmail())
        );
    }

    @Test
    public void testThatFullUpdateReturnsStatus404IfUserDoesNotExist() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        String newUsername = "Changed username";

        UserEntity savedUser = userService.save(testUser);
        savedUser.setUsername(newUsername);
        String userJson = objectMapper.writeValueAsString(savedUser);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/2137")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus200AndUpdatedUserIfUserExists() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        String newUsername = "Updated username";

        UserEntity savedUser = userService.save(testUser);

        String contentJson = objectMapper.writeValueAsString(UserEntity.builder().username(newUsername).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value(newUsername)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedUser.getEmail())
        );
    }

    @Test
    public void testThatPartialUpdateReturnsStatus404IfUserDoesNotExist() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        String newUsername = "Changed username";

        userService.save(testUser);

        String contentJson = objectMapper.writeValueAsString(UserEntity.builder().username(newUsername).build());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/2137")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatDeleteUserReturnsStatus204AndRemovesUserFromDb() throws Exception {
        UserEntity testUser = TestDataUtil.createTestUserA();
        UserEntity savedUser = userService.save(testUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );

        assertThat(userService.find(savedUser.getId())).isEmpty();
    }

}
