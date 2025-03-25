package szymanski.jakub.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ModelMapper} configuration.
 */
@Configuration
public class MapperConfig {

    /**
     * Configures {@link ModelMapper} used in application.
     *
     * @return  configured {@link ModelMapper} instance
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Allows request to create nested objects
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }

}
