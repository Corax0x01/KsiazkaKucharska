package szymanski.jakub.backend.recipe.services.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import szymanski.jakub.backend.support.utils.TestDataUtil;
import szymanski.jakub.backend.ingredient.repositories.IngredientRepository;
import szymanski.jakub.backend.recipe.TagsEnum;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.dtos.RecipeMapperImpl;
import szymanski.jakub.backend.recipe.dtos.requests.CreateRecipeRequest;
import szymanski.jakub.backend.recipe.entities.RecipeEntity;
import szymanski.jakub.backend.recipe.exceptions.RecipeNotFoundException;
import szymanski.jakub.backend.recipe.repositories.RecipeRepository;
import szymanski.jakub.backend.recipeingredients.entities.RecipeIngredientEntity;
import szymanski.jakub.backend.recipeingredients.services.impl.RecipeIngredientsServiceImpl;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.exceptions.UserNotFoundException;
import szymanski.jakub.backend.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RecipeServiceImplTest {

    @Autowired
    private RecipeServiceImpl recipeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private RecipeRepository recipeRepository;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private IngredientRepository ingredientRepository;

    @MockitoBean
    private RecipeMapperImpl recipeMapper;

    @MockitoBean
    private RecipeIngredientsServiceImpl recipeIngredientsService;

    @Nested
    public class TheFindAllMethod {

        @Test
        public void ReturnsListOfAllRecipes_WhenThereAreRecipesInDB() {
            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            null,
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            null,
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            List<RecipeDto> returnedRecipes = recipeService.findAll();

            //TODO: check for inconvertible types

            assertNotNull(returnedRecipes);
            assertEquals(2, returnedRecipes.size());
            assertEquals(recipeEntities.getFirst().getId(), returnedRecipes.getFirst().getId());
            assertEquals(recipeEntities.getFirst().getTitle(), returnedRecipes.getFirst().getTitle());
            assertEquals(recipeEntities.getFirst().getDescription(), returnedRecipes.getFirst().getDescription());
            assertEquals(recipeEntities.getFirst().getImageName(), returnedRecipes.getFirst().getImageName());
            assertEquals(recipeEntities.getFirst().getTags(), returnedRecipes.getFirst().getTags());
            assertEquals(recipeEntities.getFirst().getUserEntity(), returnedRecipes.getFirst().getUser());

            assertEquals(recipeEntities.get(1).getId(), returnedRecipes.get(1).getId());
            assertEquals(recipeEntities.get(1).getTitle(), returnedRecipes.get(1).getTitle());
            assertEquals(recipeEntities.get(1).getDescription(), returnedRecipes.get(1).getDescription());
            assertEquals(recipeEntities.get(1).getImageName(), returnedRecipes.get(1).getImageName());
            assertEquals(recipeEntities.get(1).getTags(), returnedRecipes.get(1).getTags());
            assertEquals(recipeEntities.get(1).getUserEntity(), returnedRecipes.get(1).getUser());

        }

        @Test
        public void ReturnsEmptyList_WhenThereAreNoRecipesInDB() {
            when(recipeRepository.findAll()).thenReturn(new ArrayList<>(0));

            List<RecipeDto> recipeDtos = recipeService.findAll();

            assertNotNull(recipeDtos);
            assertTrue(recipeDtos.isEmpty());
        }
    }

    @Nested
    public class TheFindAllWithPaginationMethod {

        @Test
        public void ReturnsPageWithRecipes_WhenPageablePassed() {
            Page<RecipeEntity> recipes = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "Test image name 1",
                            null,
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "Test image name 2",
                            null,
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipes);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> returnedRecipesPage = recipeService.findAllWithPagination(PageRequest.of(0, 10));

            assertNotNull(returnedRecipesPage);

            List<RecipeDto> returnedRecipesList =  returnedRecipesPage.get().toList();

            assertFalse(returnedRecipesList.isEmpty());
            assertEquals(2, returnedRecipesList.size());

            RecipeDto recipeDto1 = returnedRecipesList.getFirst();
            assertEquals(1L, recipeDto1.getId());
            assertEquals("Test recipe 1", recipeDto1.getTitle());
            assertEquals("Test description 1", recipeDto1.getDescription());
            assertEquals("Test image name 1", recipeDto1.getImageName());
            assertNull(recipeDto1.getTags());
            assertNull(recipeDto1.getUser());

            RecipeDto recipeDto2 = returnedRecipesList.get(1);
            assertEquals(2L, recipeDto2.getId());
            assertEquals("Test recipe 2", recipeDto2.getTitle());
            assertEquals("Test description 2", recipeDto2.getDescription());
            assertEquals("Test image name 2", recipeDto2.getImageName());
            assertNull(recipeDto2.getTags());
            assertNull(recipeDto2.getUser());
        }

        @Test
        public void ReturnsEmptyPage_WhenThereAreNoRecipesInDB() {
            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> returnedRecipesPage = recipeService.findAllWithPagination(PageRequest.of(0, 10));

            assertNotNull(returnedRecipesPage);
            assertTrue(returnedRecipesPage.isEmpty());
        }
    }

    @Nested
    public class TheFindRecipeByTagsMethod {

        @Test
        public void ReturnsListWithOneRecipe_WhenListOfTagsPassedAndOneOfTwoRecipesFits() {

            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            List<RecipeDto> recipes = recipeService.findRecipeByTags(List.of(TagsEnum.DESSERT, TagsEnum.COOKIES));

            assertNotNull(recipes);
            assertEquals(1, recipes.size());
            assertEquals(recipeMapper.mapTo(recipeEntities.get(1)), recipes.getFirst());
        }

        @Test
        public void ReturnsListWithTwoRecipes_WhenListOfTagsPassedAndMultipleRecipesFits() {

            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            List<RecipeDto> recipes = recipeService.findRecipeByTags(List.of(TagsEnum.DESSERT));

            assertNotNull(recipes);
            assertEquals(2, recipes.size());
            assertEquals(recipeMapper.mapTo(recipeEntities.getFirst()), recipes.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get(1)), recipes.get(1));
        }

        @Test
        public void ReturnsListWithAllRecipes_WhenEmptyListPassed() {

            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            3L,
                            "Test recipe 3",
                            "Test description 3",
                            "",
                            List.of(TagsEnum.DRINK),
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            List<RecipeDto> recipes = recipeService.findRecipeByTags(Collections.emptyList());

            assertNotNull(recipes);
            assertEquals(3, recipes.size());
            assertEquals(recipeMapper.mapTo(recipeEntities.getFirst()), recipes.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get(1)), recipes.get(1));
            assertEquals(recipeMapper.mapTo(recipeEntities.get(2)), recipes.get(2));
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassed() {

            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            assertThrows(NullPointerException.class, () -> recipeService.findRecipeByTags(null));
        }

        @Test
        public void ReturnsEmptyList_WhenNoRecipeMatchesTags() {
            List<RecipeEntity> recipeEntities = List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            );

            when(recipeRepository.findAll()).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            List<RecipeDto> recipes = recipeService.findRecipeByTags(List.of(TagsEnum.MAIN_COURSE));

            assertNotNull(recipes);
            assertEquals(0, recipes.size());
        }
    }

    @Nested
    public class TheFindAllByTagsMethod {

        @Test
        public void ReturnsPageWithOneRecipe_WhenListOfTagsPassedAndOneOfTwoRecipesFits() {

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByTags(List.of(TagsEnum.DESSERT, TagsEnum.COOKIES), PageRequest.of(0, 10));

            assertNotNull(recipesPage);

            List<RecipeDto> recipesList = recipesPage.get().toList();

            assertEquals(1, recipesList.size());
            assertEquals(0, recipesPage.getNumber());
            assertEquals(10, recipesPage.getSize());
            assertEquals(1, recipesPage.getNumberOfElements());
            assertEquals(1, recipesPage.getTotalElements());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(1)), recipesList.getFirst());
        }

        @Test
        public void ReturnsPageWithTwoRecipes_WhenListOfTagsPassedAndMultipleRecipesFits() {

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByTags(List.of(TagsEnum.DESSERT), PageRequest.of(0, 10));

            assertNotNull(recipesPage);

            List<RecipeDto> recipesList = recipesPage.get().toList();

            assertEquals(2, recipesList.size());
            assertEquals(0, recipesPage.getNumber());
            assertEquals(10, recipesPage.getSize());
            assertEquals(2, recipesPage.getNumberOfElements());
            assertEquals(2, recipesPage.getTotalElements());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().getFirst()), recipesList.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(1)), recipesList.get(1));
        }

        @Test
        public void ReturnsPageWithAllRecipes_WhenEmptyListPassed() {

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            3L,
                            "Test recipe 3",
                            "Test description 3",
                            "",
                            List.of(TagsEnum.DRINK),
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByTags(Collections.emptyList(), PageRequest.of(0, 10));

            assertNotNull(recipesPage);

            List<RecipeDto> recipesList = recipesPage.get().toList();

            assertEquals(3, recipesList.size());
            assertEquals(0, recipesPage.getNumber());
            assertEquals(10, recipesPage.getSize());
            assertEquals(3, recipesPage.getNumberOfElements());
            assertEquals(3, recipesPage.getTotalElements());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().getFirst()), recipesList.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(1)), recipesList.get(1));
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(2)), recipesList.get(2));
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassed() {

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            assertThrows(NullPointerException.class, () -> recipeService.findAllByTags(null, PageRequest.of(0, 10)));
        }

        @Test
        public void ReturnsEmptyPage_WhenNoRecipeMatchesTags() {
            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.CAKE),
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "",
                            List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                            null,
                            null)
            ));

            when(recipeRepository.findAll(any(Pageable.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByTags(List.of(TagsEnum.MAIN_COURSE), PageRequest.of(0, 10));

            assertNotNull(recipesPage);
            assertEquals(0, recipesPage.get().toList().size());
        }
    }

    @Nested
    public class TheFindAllByAuthorMethod_ByAuthentication {

        @Test
        public void ReturnsPageWithRecipesCreatedByGivenUser_WhenAuthenticationProvided() {
            Optional<UserEntity> userToReturn = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "Test image name 1",
                            null,
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "Test image name 2",
                            null,
                            null,
                            null)
            ));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userToReturn.get(),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );

            when(userRepository.findById(anyLong())).thenReturn(userToReturn);
            when(recipeRepository.findAllByUserEntity(any(Pageable.class), any(UserEntity.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByAuthor(PageRequest.of(0, 10), token);

            assertNotNull(recipesPage);

            List<RecipeDto> recipesList = recipesPage.get().toList();

            assertEquals(2, recipesList.size());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().getFirst()), recipesList.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(1)), recipesList.get(1));
        }

        @Test
        public void ThrowsUserNotFoundException_WhenAuthenticatedUserNotFound() {
            String message = "Test message thrown";
            Optional<UserEntity> userToReturn = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userToReturn.get(),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );

            doThrow(new UserNotFoundException(message)).when(userRepository).findById(anyLong());

            assertThrows(UserNotFoundException.class,
                    () -> recipeService.findAllByAuthor(PageRequest.of(0, 10), token)
            );
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsAuthentication() {
            assertThrows(NullPointerException.class,
                    () -> recipeService.findAllByAuthor(PageRequest.of(0, 10), (Authentication) null));
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsPageable() {
            UserEntity user = TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            );

            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

            assertThrows(
                    NullPointerException.class,
                    () -> recipeService.findAllByAuthor(null, new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority("USER"))
                    ))
            );
        }
    }

    @Nested
    public class TheFindAllByAuthorMethod_ById {

        @Test
        public void ReturnsPageWithRecipesCreatedByGivenUser_WhenUserWithGivenIdExists() {
            Optional<UserEntity> userToReturn = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));

            Page<RecipeEntity> recipeEntities = new PageImpl<>(List.of(
                    TestDataUtil.createTestRecipeEntity(
                            1L,
                            "Test recipe 1",
                            "Test description 1",
                            "Test image name 1",
                            null,
                            null,
                            null),
                    TestDataUtil.createTestRecipeEntity(
                            2L,
                            "Test recipe 2",
                            "Test description 2",
                            "Test image name 2",
                            null,
                            null,
                            null)
            ));

            when(userRepository.findById(anyLong())).thenReturn(userToReturn);
            when(recipeRepository.findAllByUserEntity(any(Pageable.class), any(UserEntity.class))).thenReturn(recipeEntities);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            Page<RecipeDto> recipesPage = recipeService.findAllByAuthor(PageRequest.of(0, 10), 1L);

            assertNotNull(recipesPage);

            List<RecipeDto> recipesList = recipesPage.get().toList();

            assertEquals(2, recipesList.size());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().getFirst()), recipesList.getFirst());
            assertEquals(recipeMapper.mapTo(recipeEntities.get().toList().get(1)), recipesList.get(1));
        }

        @Test
        public void ThrowsUserNotFoundException_WhenUserWithGivenIdNotFound() {
            String message = "Test message thrown";
            Optional<UserEntity> userToReturn = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));

            doThrow(new UserNotFoundException(message)).when(userRepository).findById(anyLong());

            assertThrows(UserNotFoundException.class,
                    () -> recipeService.findAllByAuthor(PageRequest.of(0, 10), 1L)
            );
        }

        @Test
        public void ThrowsUserNotFoundException_WhenNullPassedAsId() {
            assertThrows(UserNotFoundException.class,
                    () -> recipeService.findAllByAuthor(PageRequest.of(0, 10), (Long) null)
            );
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsPageable() {
            UserEntity user = TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            );

            when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

            assertThrows(
                    NullPointerException.class,
                    () -> recipeService.findAllByAuthor(null, 1L)
            );
        }
    }

    @Nested
    public class TheFindMethod {

        @Test
        public void ReturnsRecipeDto_WhenRecipeWithGivenIdExists() {
            Optional<RecipeEntity> recipe = Optional.of(TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.MAIN_COURSE, TagsEnum.MEAT),
                    null,
                    null
            ));

            when(recipeRepository.findById(anyLong())).thenReturn(recipe);
            when(recipeMapper.mapTo(any(RecipeEntity.class))).then(r -> {
                RecipeEntity recipeEntity = r.getArgument(0);
                return RecipeDto.builder()
                        .id(recipeEntity.getId())
                        .title(recipeEntity.getTitle())
                        .description(recipeEntity.getDescription())
                        .imageName(recipeEntity.getImageName())
                        .tags(recipeEntity.getTags())
                        .user(null)
                        .build();
            });

            RecipeDto returnedRecipe = recipeService.find(1L);

            assertNotNull(returnedRecipe);
            assertEquals(recipe.get().getId(), returnedRecipe.getId());
            assertEquals(recipe.get().getTitle(), returnedRecipe.getTitle());
            assertEquals(recipe.get().getDescription(), returnedRecipe.getDescription());
            assertEquals(recipe.get().getImageName(), returnedRecipe.getImageName());
            assertEquals(recipe.get().getTags(), returnedRecipe.getTags());
            assertNull(returnedRecipe.getUser());
        }

        @Test
        public void ThrowsRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() {
            when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

            RecipeNotFoundException exception = assertThrows(
                    RecipeNotFoundException.class, () -> recipeService.find(1L));
            assertEquals("Recipe with id: 1 not found", exception.getMessage());
        }

        @Test
        public void ThrowsRecipeNotFoundException_WhenNullPassedAsParameter() {
            assertThrows(RecipeNotFoundException.class, () -> recipeService.find(null));
        }
    }

    @Nested
    public class TheCreateMethod {

        @Test
        public void ReturnsIdOfCreatedRecipeAndCreatesCorrectRecipeIngredients_WhenRequestValidAndAuthenticated() {
            ArgumentCaptor<RecipeIngredientEntity> recipeIngredientCaptor = ArgumentCaptor.forClass(RecipeIngredientEntity.class);
            CreateRecipeRequest recipeRequest = TestDataUtil.createTestCreateRecipeRequest(
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                    List.of(
                            TestDataUtil.createTestIngredientQuantityDto("Jajka", "2 sztuki"),
                            TestDataUtil.createTestIngredientQuantityDto("Pieprz", "szczypta"),
                            TestDataUtil.createTestIngredientQuantityDto("Sól", "szczypta")
                    )
            );

            Optional<UserEntity> userToReturn = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));

            RecipeEntity recipe = TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                    null,
                    Collections.emptyList()
            );

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userToReturn.get(),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );

            when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipe);
            when(ingredientRepository.findByName(anyString())).thenReturn(Optional.empty());

            Long savedRecipeId = recipeService.create(recipeRequest, token);

            assertEquals(1L, savedRecipeId);
            verify(recipeIngredientsService, times(3)).save(recipeIngredientCaptor.capture());

            List<RecipeIngredientEntity> capturedRecipeIngredients = recipeIngredientCaptor.getAllValues();

            assertNotNull(capturedRecipeIngredients);
            assertEquals(3, capturedRecipeIngredients.size());

            assertEquals(recipe, capturedRecipeIngredients.getFirst().getRecipeEntity());
            assertEquals("Jajka" , capturedRecipeIngredients.getFirst().getIngredientEntity().getName());
            assertEquals("2 sztuki" , capturedRecipeIngredients.getFirst().getQuantity());

            assertEquals(recipe, capturedRecipeIngredients.get(1).getRecipeEntity());
            assertEquals("Pieprz" , capturedRecipeIngredients.get(1).getIngredientEntity().getName());
            assertEquals("szczypta" , capturedRecipeIngredients.get(1).getQuantity());

            assertEquals(recipe, capturedRecipeIngredients.get(2).getRecipeEntity());
            assertEquals("Sól" , capturedRecipeIngredients.get(2).getIngredientEntity().getName());
            assertEquals("szczypta" , capturedRecipeIngredients.get(2).getQuantity());
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsRequest() {
            Optional<UserEntity> user = Optional.of(TestDataUtil.createTestUserEntity(
                    1L,
                    "Test user",
                    passwordEncoder.encode("P@ssw0rd"),
                    "test@mail.com",
                    true,
                    false,
                    null,
                    null
            ));
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.get(),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );

            assertThrows(NullPointerException.class, () -> recipeService.create(null, token));
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassedAsAuthentication() {
            CreateRecipeRequest recipeRequest = TestDataUtil.createTestCreateRecipeRequest(
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT, TagsEnum.COOKIES),
                    List.of(
                            TestDataUtil.createTestIngredientQuantityDto("Jajka", "2 sztuki"),
                            TestDataUtil.createTestIngredientQuantityDto("Pieprz", "szczypta"),
                            TestDataUtil.createTestIngredientQuantityDto("Sól", "szczypta")
                    )
            );

            assertThrows(NullPointerException.class, () -> recipeService.create(recipeRequest, null));
        }
    }

    @Nested
    public class TheSaveMethod {

        @Test
        public void ReturnsIdOfSavedRecipe_WhenValidRecipeDtoPassed() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT),
                    null
            );
            RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT),
                    null,
                    Collections.emptyList()
            );

            when(recipeMapper.mapFrom(any(RecipeDto.class))).then(r -> {
                RecipeDto recipeDto = r.getArgument(0);
                return RecipeEntity.builder()
                        .id(recipeDto.getId())
                        .title(recipeDto.getTitle())
                        .description(recipeDto.getDescription())
                        .imageName(recipeDto.getImageName())
                        .tags(recipeDto.getTags())
                        .userEntity(null)
                        .recipeIngredients(Collections.emptyList())
                        .build();
            });
            when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

            Long savedRecipeId = recipeService.save(recipe);

            assertEquals(1L, savedRecipeId);
        }

        @Test
        public void ThrowsNullPointerException_WhenNullPassed() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DESSERT),
                    null
            );
            assertThrows(NullPointerException.class, () -> recipeService.save(recipe));
        }
    }

    @Nested
    public class ThePartialUpdateMethod {

        @Test
        public void ReturnsUpdatedRecipeId_WhenRecipeWithGivenIdExists() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    null,
                    "Test recipe",
                    "Test description",
                    null,
                    null,
                    null
            );
            Optional<RecipeEntity> recipeEntity = Optional.of(TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe 1",
                    "Test description 1",
                    "Test image",
                    List.of(),
                    null,
                    Collections.emptyList()
            ));

            when(recipeRepository.findById(anyLong())).thenReturn(recipeEntity);
            when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity.get());

            Long updatedRecipeId = recipeService.partialUpdate(1L, recipe);

            assertEquals(1L, updatedRecipeId);
        }

        @Test
        public void ThrowsRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    null,
                    "Test recipe",
                    "Test description",
                    null,
                    null,
                    null
            );

            when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

            RecipeNotFoundException exception = assertThrows(
                    RecipeNotFoundException.class, () -> recipeService.partialUpdate(1L, recipe));
            assertEquals("Recipe with id: 1 not found", exception.getMessage());
        }

    }

    @Nested
    public class TheDeleteMethod_ById {

        @Test
        public void RepositoryDeleteMethodCalled_WhenRecipeWithGivenIdExists() {
            when(recipeRepository.existsById(anyLong())).thenReturn(true);

            recipeService.delete(1L);

            verify(recipeRepository, times(1)).deleteById(anyLong());
        }

        @Test
        public void ThrowsRecipeNotFoundException_WhenRecipeWithGivenIdNotExists() {
            when(recipeRepository.existsById(anyLong())).thenReturn(false);

            assertThrows(RecipeNotFoundException.class, () -> recipeService.delete(1L));
        }
    }

    @Nested
    public class TheDeleteMethod_ByRecipeDto {
        @Test
        public void RepositoryDeleteMethodCalled_WhenRecipeExists() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DRINK),
                    null
            );

            when(recipeRepository.existsById(anyLong())).thenReturn(true);
            when(recipeMapper.mapFrom(any(RecipeDto.class))).then(r -> {
                RecipeDto recipeDto = r.getArgument(0);
                return RecipeEntity.builder()
                        .id(recipeDto.getId())
                        .title(recipeDto.getTitle())
                        .description(recipeDto.getDescription())
                        .imageName(recipeDto.getImageName())
                        .tags(recipeDto.getTags())
                        .userEntity(null)
                        .recipeIngredients(Collections.emptyList())
                        .build();
            });

            recipeService.delete(recipe);

            verify(recipeRepository, times(1)).delete(any(RecipeEntity.class));
        }

        @Test
        public void ThrowsRecipeNotFoundException_WhenRecipeNotExists() {
            RecipeDto recipe = TestDataUtil.createTestRecipeDto(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DRINK),
                    null
            );

            when(recipeRepository.existsById(anyLong())).thenReturn(false);
            when(recipeMapper.mapFrom(any(RecipeDto.class))).then(r -> {
                RecipeDto recipeDto = r.getArgument(0);
                return RecipeEntity.builder()
                        .id(recipeDto.getId())
                        .title(recipeDto.getTitle())
                        .description(recipeDto.getDescription())
                        .imageName(recipeDto.getImageName())
                        .tags(recipeDto.getTags())
                        .userEntity(null)
                        .recipeIngredients(Collections.emptyList())
                        .build();
            });

            assertThrows(RecipeNotFoundException.class, () -> recipeService.delete(recipe));
        }
    }

    @Nested
    public class TheExistsMethod_ById {

        @Test
        public void ReturnsTrue_WhenRecipeWithGivenIdExists() {
            when(recipeRepository.existsById(anyLong())).thenReturn(true);

            assertTrue(recipeService.exists(1L));
        }

        @Test
        public void ReturnsFalse_WhenRecipeWithGivenIdNotExists() {
            when(recipeRepository.existsById(anyLong())).thenReturn(false);

            assertFalse(recipeService.exists(1L));
        }
    }

    @Nested
    public class TheExistsMethod_ByRecipeEntity {

        @Test
        public void ReturnsTrue_WhenGivenRecipeExists() {
            RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DRINK),
                    null,
                    null
            );

            when(recipeRepository.existsById(anyLong())).thenReturn(true);

            assertTrue(recipeService.exists(recipeEntity));
        }

        @Test
        public void ReturnsFalse_WhenGivenRecipeNotExists() {
            RecipeEntity recipeEntity = TestDataUtil.createTestRecipeEntity(
                    1L,
                    "Test recipe",
                    "Test description",
                    "Test image",
                    List.of(TagsEnum.DRINK),
                    null,
                    null
            );

            when(recipeRepository.existsById(anyLong())).thenReturn(false);

            assertFalse(recipeService.exists(recipeEntity));
        }
    }
}