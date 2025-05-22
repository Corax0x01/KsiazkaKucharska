package szymanski.jakub.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
