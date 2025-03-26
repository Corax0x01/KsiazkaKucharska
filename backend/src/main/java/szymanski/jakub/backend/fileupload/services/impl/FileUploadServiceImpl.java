package szymanski.jakub.backend.fileupload.services.impl;

import lombok.extern.java.Log;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.exceptions.FileUploadException;
import szymanski.jakub.backend.fileupload.exceptions.UploadedFileNotFoundException;
import szymanski.jakub.backend.fileupload.services.FileUploadService;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.services.RecipeService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Log
@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Path rootLocation;
    private final RecipeService recipeService;

    /**
     * Class constructor.
     *
     * @param   properties          file upload {@link FileUploadProperties properties}
     * @param   recipeService       instance of {@link RecipeService}
     * @throws  FileUploadException if error with uploading file occurs
     */
    public FileUploadServiceImpl(FileUploadProperties properties, RecipeService recipeService) {
        if(properties.getLocation().trim().isEmpty()) {
            throw new FileUploadException("File upload location can not be empty");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.recipeService = recipeService;
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new FileUploadException("Could not initialize file upload", e);
        }
    }

    public void upload(MultipartFile file, Long recipeId) {
        try {
            if(file.isEmpty()) {
                throw new FileUploadException("Failed to upload empty file");
            }

            if(!recipeService.exists(recipeId)) {
                throw new FileUploadException("Failed to upload file for non-existent recipe");
            }

            Path destinationFile = this.rootLocation.resolve(Paths.get(
                        Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();

            if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new FileUploadException("Cannot upload file outside current directory");
            }

//          Change filename to UUID
            String filename = destinationFile.getFileName().toString();
            int dotIndex = filename.lastIndexOf('.');
            String fileExtension = filename.substring(dotIndex + 1);
            String newFilename = UUID.randomUUID() + "." + fileExtension;
            Path newFilePath = Paths.get(destinationFile.getParent() + "/" + newFilename);

            recipeService.partialUpdate(recipeId, RecipeDto.builder().imageName(newFilename).build());

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new FileUploadException("Error when uploading file", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            } else {
                throw new UploadedFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new FileUploadException("Could not read file" + filename, e);
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        log.info("All images deleted");
    }

    public void delete(String filename) {
        try {
            FileSystemUtils.deleteRecursively(load(filename));
        } catch (IOException e) {
            throw new FileUploadException("Could not delete file:" + filename, e);
        }
    }
}
