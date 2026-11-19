package szymanski.jakub.backend.common.exceptionhandler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import szymanski.jakub.backend.security.JwtFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static szymanski.jakub.backend.common.exceptionhandler.BusinessErrorCodesEnum.*;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = ExceptionThrowingController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void RespondsWithBadCredentialsException_WhenThrown() throws Exception {
        String type = "org.springframework.security.authentication.BadCredentialsException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(BAD_CREDENTIALS.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(BAD_CREDENTIALS.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(BAD_CREDENTIALS.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithMessagingException_WhenThrown() throws Exception {
        String type = "jakarta.mail.MessagingException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithTokenExpiredException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.auth.exceptions.TokenExpiredException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(TOKEN_EXPIRED.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(TOKEN_EXPIRED.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(TOKEN_EXPIRED.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithTokenNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.auth.exceptions.TokenNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(TOKEN_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(TOKEN_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(TOKEN_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithException_WhenThrown() throws Exception {
        String type = "java.lang.Exception";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.businessErrorDescription").value("Internal server error"))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithLockedException_WhenThrown() throws Exception {
        String type = "org.springframework.security.authentication.LockedException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                .param("type", type)
                .param("message", message)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ACCOUNT_LOCKED.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(ACCOUNT_LOCKED.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(ACCOUNT_LOCKED.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithDisabledException_WhenThrown() throws Exception {
        String type = "org.springframework.security.authentication.DisabledException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ACCOUNT_DISABLED.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(ACCOUNT_DISABLED.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(ACCOUNT_DISABLED.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithFileUploadException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.fileupload.exceptions.FileUploadException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(FILE_NOT_UPLOADED.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(FILE_NOT_UPLOADED.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(FILE_NOT_UPLOADED.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithFileNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.fileupload.exceptions.UploadedFileNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(FILE_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(FILE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(FILE_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithRoleNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.role.exceptions.RoleNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(ROLE_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(ROLE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(ROLE_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithUserNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.user.exceptions.UserNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(USER_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(USER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(USER_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithRecipeNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(RECIPE_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithIngredientNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.ingredient.exceptions.IngredientNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(INGREDIENT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(INGREDIENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(INGREDIENT_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }

    @Test
    public void RespondsWithRecipeIngredientNotFoundException_WhenThrown() throws Exception {
        String type = "szymanski.jakub.backend.recipeingredients.exceptions.RecipeIngredientNotFoundException";
        String message = "Tested exception thrown";

        this.mockMvc.perform(get("/test/exceptions")
                        .param("type", type)
                        .param("message", message)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(RECIPE_INGREDIENT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.businessErrorCode").value(RECIPE_INGREDIENT_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.businessErrorDescription").value(RECIPE_INGREDIENT_NOT_FOUND.getDescription()))
                .andExpect(jsonPath("$.error").value(message));
    }
}