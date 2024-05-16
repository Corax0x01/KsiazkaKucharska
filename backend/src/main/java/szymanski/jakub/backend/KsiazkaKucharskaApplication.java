package szymanski.jakub.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.services.FileUploadService;
import szymanski.jakub.backend.role.entities.RoleEntity;
import szymanski.jakub.backend.role.repositories.RoleRepository;
import szymanski.jakub.backend.user.entities.UserEntity;
import szymanski.jakub.backend.user.repositories.UserRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(FileUploadProperties.class)
@EnableJpaAuditing
@EnableAsync
public class KsiazkaKucharskaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KsiazkaKucharskaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(
			FileUploadService fileUploadService,
			RoleRepository roleRepository,
			UserRepository userRepository) {
		return (args -> {
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(RoleEntity.builder().name("USER").build());
			}
			if(roleRepository.findByName("ADMIN").isEmpty()) {
				roleRepository.save(RoleEntity.builder().name("ADMIN").build());
			}
			if(userRepository.findByRoles(List.of(roleRepository.findByName("ADMIN").orElseThrow())).isEmpty()) {
				userRepository.save(UserEntity.builder()
						.username("Admin")
						.password("e&s0%DYIwrCUc4B")
						.email("Corax01@wp.pl")
						.roles(List.of(roleRepository.findByName("ADMIN").orElseThrow(() ->
								new RoleNotFoundException("Role ADMIN not initialized")
						)))
						.locked(false)
						.enabled(true)
						.build()
				);
			}
			fileUploadService.deleteAll();
			fileUploadService.init();
		});
	}

}
