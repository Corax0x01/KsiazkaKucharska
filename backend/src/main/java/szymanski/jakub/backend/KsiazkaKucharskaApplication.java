package szymanski.jakub.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.services.FileUploadService;

@SpringBootApplication
@EnableConfigurationProperties(FileUploadProperties.class)
public class KsiazkaKucharskaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KsiazkaKucharskaApplication.class, args);
	}

	@Bean
	CommandLineRunner init(FileUploadService fileUploadService) {
		return (args -> {
			fileUploadService.deleteAll();
			fileUploadService.init();
		});
	}

}
