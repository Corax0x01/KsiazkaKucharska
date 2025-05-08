package szymanski.jakub.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

/**
 * Properties of file upload functionality
 */
@Getter
@Setter
@ConfigurationProperties("file-upload")
public class FileUploadProperties {

    /**
     * File upload directory name
     */
    private String location;

}
