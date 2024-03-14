package szymanski.jakub.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import szymanski.jakub.backend.domain.TagsEnum;
import szymanski.jakub.backend.domain.entities.*;
import szymanski.jakub.backend.services.IngredientService;
import szymanski.jakub.backend.services.RecipeIngredientsService;
import szymanski.jakub.backend.services.RecipeService;
import szymanski.jakub.backend.services.UserService;

import java.util.List;

@SpringBootApplication
public class KsiazkaKucharskaApplication implements CommandLineRunner {

	private final UserService userService;
	private final IngredientService ingredientService;
	private final RecipeService recipeService;
	private final RecipeIngredientsService recipeIngredientsService;

	@Autowired
	public KsiazkaKucharskaApplication(UserService userService, IngredientService ingredientService, RecipeService recipeService, RecipeIngredientsService recipeIngredientsService) {
		this.userService = userService;
		this.ingredientService = ingredientService;
		this.recipeService = recipeService;
		this.recipeIngredientsService = recipeIngredientsService;
	}

	public static void main(String[] args) {
		SpringApplication.run(KsiazkaKucharskaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		UserEntity user1 = userService.save(
				UserEntity.builder()
				.id(1L)
				.username("Corax01")
				.password("passw0rd")
				.email("Corax01@wp.pl")
				.isAdmin(false)
				.build()
		);

		UserEntity user2 = userService.save(
				UserEntity.builder()
				.id(2L)
				.username("Szykuba")
				.password("passw0rd")
				.email("szykuba@gmail.com")
				.isAdmin(false)
				.build()
		);

		IngredientEntity ingredient1 = ingredientService.save(
				IngredientEntity.builder()
						.id(1L)
						.name("Jajka")
						.build()
		);

		IngredientEntity ingredient2 = ingredientService.save(
				IngredientEntity.builder()
						.id(2L)
						.name("Mąka")
						.build()
		);

		IngredientEntity ingredient3 = ingredientService.save(
				IngredientEntity.builder()
						.id(3L)
						.name("Sól")
						.build()
		);

		IngredientEntity ingredient4 = ingredientService.save(
				IngredientEntity.builder()
						.id(4L)
						.name("Kiełbasa")
						.build()
		);



		RecipeEntity recipe1 = recipeService.save(
				RecipeEntity.builder()
						.id(1L)
						.title("Naleśniki")
						.description("Wymieszać i usmażyc")
						.imageURL("testImage")
						.recipeURL("testRecipe")
						.userEntity(user1)
						.tags(List.of(TagsEnum.BREAKFAST, TagsEnum.DESSERT, TagsEnum.VEGETARIAN))
						.build()
		);

		RecipeEntity recipe2 = recipeService.save(
				RecipeEntity.builder()
						.id(2L)
						.title("Jajecznica z kiełbasą")
						.description("Wymieszać i usmażyc")
						.imageURL("testImage")
						.recipeURL("testRecipe")
						.userEntity(user2)
						.tags(List.of(TagsEnum.BREAKFAST, TagsEnum.MEAT))
						.build()
		);

		RecipeEntity recipe3 = recipeService.save(
				RecipeEntity.builder()
						.id(3L)
						.title("Jajecznica wege")
						.description("Wymieszać i usmażyc")
						.imageURL("testImage")
						.recipeURL("testRecipe")
						.userEntity(user2)
						.tags(List.of(TagsEnum.BREAKFAST, TagsEnum.VEGETARIAN))
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe1.getId(), ingredient1.getId()))
						.ingredientEntity(ingredient1)
						.recipeEntity(recipe1)
						.quantity("2 szt.")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe1.getId(), ingredient2.getId()))
						.ingredientEntity(ingredient2)
						.recipeEntity(recipe1)
						.quantity("200 gram")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe2.getId(), ingredient1.getId()))
						.ingredientEntity(ingredient1)
						.recipeEntity(recipe2)
						.quantity("3 sztuki")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe2.getId(), ingredient3.getId()))
						.ingredientEntity(ingredient3)
						.recipeEntity(recipe2)
						.quantity("Szczypta")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe2.getId(), ingredient4.getId()))
						.ingredientEntity(ingredient4)
						.recipeEntity(recipe2)
						.quantity("100 gram")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe3.getId(), ingredient1.getId()))
						.ingredientEntity(ingredient1)
						.recipeEntity(recipe3)
						.quantity("3 sztuki")
						.build()
		);

		recipeIngredientsService.save(
				RecipeIngredientEntity.builder()
						.id(new RecipeIngredientKey(recipe3.getId(), ingredient3.getId()))
						.ingredientEntity(ingredient3)
						.recipeEntity(recipe3)
						.quantity("Szczypta")
						.build()
		);
	}
}
