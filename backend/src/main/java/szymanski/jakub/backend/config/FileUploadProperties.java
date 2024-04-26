package szymanski.jakub.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("file-upload")
public class FileUploadProperties {

    private String location = "upload_dir";

}
