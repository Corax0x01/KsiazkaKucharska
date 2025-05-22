package szymanski.jakub.backend;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import szymanski.jakub.backend.config.AdminAccountConfig;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.services.FileUploadService;
import szymanski.jakub.backend.ingredient.dtos.IngredientDto;
import szymanski.jakub.backend.ingredient.services.IngredientService;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.services.RecipeService;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.services.UserService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static szymanski.jakub.backend.recipe.TagsEnum.*;

@Log
@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties({FileUploadProperties.class, AdminAccountConfig.class})
public class KsiazkaKucharskaApplication {

	@Value("${application.db-init.filepath.ingredients}")
	private String ingredientsInitDataFilePath;

	public static void main(String[] args) {
		SpringApplication.run(KsiazkaKucharskaApplication.class, args);
	}

	public void initDBWithIngredients(IngredientService ingredientService) {
		List<String> ingredientNames;
		try (Scanner scanner = new Scanner(new File(ingredientsInitDataFilePath))) {
			String data = scanner.nextLine();
			ingredientNames = Arrays.stream(data.split(",")).toList();

			for(String ingredient : ingredientNames) {
				ingredientService.save(
						IngredientDto.builder()
								.name(ingredient)
								.build()
				);
			}

			log.info("DB initialized with ingredients");
		} catch (FileNotFoundException e) {
			log.warning("DB not initialized with ingredients. Error while opening a file.");
			e.printStackTrace();
		}
	}

	/**
	 * Initializes database with user roles and creates admin account.
	 */
	@Bean
	CommandLineRunner init(
			UserService userService,
			RecipeService recipeService,
			IngredientService ingredientService,
			FileUploadService fileUploadService,
			RoleRepository roleRepository,
			AdminAccountConfig adminAccountConfig) {

		return (args -> {
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(RoleEntity.builder().name("USER").build());
				log.info("USER role created");
			}
			if(roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(RoleEntity.builder().name("ADMIN").build());
				log.info("ADMIN role created");
			}
			if(userService.findByRoles(
					List.of(
							roleRepository
									.findByName("ADMIN")
									.orElseThrow(
											() -> new szymanski.jakub.backend.role.exceptions.RoleNotFoundException(
															"Role ADMIN not found"
													)
									)
					))
					.isEmpty()) {
				UserDto admin = UserDto.builder()
						.username(adminAccountConfig.getUsername())
						.password(adminAccountConfig.getPassword())
						.email(adminAccountConfig.getEmail())
						.build();

				userService.save(admin, true, false, List.of(roleRepository.findByName("ADMIN")
						.orElseThrow(
								() -> new RoleNotFoundException("Role ADMIN not found")
						)));
				log.info("Admin account created");
			} else {
				log.warning("Admin account failed to create");
			}

			initDBWithIngredients(ingredientService);

			if(!userService.findAll().isEmpty()){
				RecipeDto recipe = RecipeDto.builder()
						.title("TEST")
						.user(userService.findAll().getFirst())
						.description("Test description")
						.imageName("test_image_name.jpg")
						.tags(List.of(MAIN_COURSE, MEAT, FAST_FOOD))
						.build();
				recipeService.save(recipe);
				log.info("Test recipe created");
			} else {
				log.warning("Test recipe failed to create");
			}

			fileUploadService.deleteAll();
			fileUploadService.init();
		});
	}
}
