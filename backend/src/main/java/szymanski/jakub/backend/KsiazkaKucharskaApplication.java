package szymanski.jakub.backend;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import szymanski.jakub.backend.common.Mapper;
import szymanski.jakub.backend.config.AdminAccountConfig;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.services.FileUploadService;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.services.RecipeService;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.exceptions.RoleNotFoundException;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.user.dtos.UserDto;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.services.UserService;

import java.util.List;

import static szymanski.jakub.backend.recipe.TagsEnum.*;

@SpringBootApplication
@EnableConfigurationProperties({FileUploadProperties.class, AdminAccountConfig.class})
@EnableJpaAuditing
@EnableAsync
@Log
public class KsiazkaKucharskaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KsiazkaKucharskaApplication.class, args);
	}

	/**
	 * Initializes database with user roles and creates admin account.
	 */
	@Bean
	CommandLineRunner init(
			FileUploadService fileUploadService,
			RoleRepository roleRepository,
			UserService userService,
			Mapper<UserEntity, UserDto> userMapper,
			RecipeService recipeService,
			AdminAccountConfig adminAccountConfig,
			PasswordEncoder passwordEncoder) {

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
