package szymanski.jakub.backend.fileupload.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import szymanski.jakub.backend.fileupload.services.FileUploadService;

@RequestMapping("files")
@RestController
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(
            @PathVariable("filename") String filename) {

        Resource file = fileUploadService.loadAsResource(filename);

        return ResponseEntity.ok(file);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("recipeId") Long recipeId)   {

        fileUploadService.upload(file, recipeId);

    }

    @DeleteMapping("/{filename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(
            @PathVariable("filename") String filename) {

        fileUploadService.delete(filename);
    }
}
