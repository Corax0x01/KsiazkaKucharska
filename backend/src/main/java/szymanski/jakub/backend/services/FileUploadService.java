package szymanski.jakub.backend.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileUploadService {

    void init();
    void upload(MultipartFile file, Long recipeId);
    Path load(String filename);
    Resource loadAsResource(String filename);
    void deleteAll();
    void delete(String filename);
}
