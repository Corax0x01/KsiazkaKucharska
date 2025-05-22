package szymanski.jakub.backend.fileupload.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import szymanski.jakub.backend.common.exceptionhandler.ExceptionResponse;
import szymanski.jakub.backend.fileupload.services.FileUploadService;

@Tag(name = "File upload")
@RestController
@RequestMapping("files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * Fetches file with given filename
     *
     * @param   filename    name of file
     * @return              {@link ResponseEntity} containing {@link Resource file} with given name
     */
    @Operation(summary = "Fetches file with given filename")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File fetched",
            content = @Content(mediaType = "multipart/form-data, application/json",
                schema = @Schema(implementation = Resource.class))),
        @ApiResponse(responseCode = "403", description = "Not authorized",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "File not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getFile(
            @Parameter(description = "Name of file")
            @PathVariable("filename") String filename) {

        Resource file = fileUploadService.loadAsResource(filename);

        return ResponseEntity.ok(file);
    }

    /**
     * Uploads file.
     *
     * @param   file        {@link MultipartFile file} to upload
     * @param   recipeId    ID of recipe that file is attached to
     */
    @Operation(summary = "Uploads file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Uploads file"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "500", description = "Error when uploading file",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void uploadFile(
            @RequestBody(description = "File to upload", content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(implementation = MultipartFile.class)
            ))
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "ID of recipe that file is attached to")
            @RequestParam("recipeId") Long recipeId)   {

        fileUploadService.upload(file, recipeId);
    }

    /**
     * Deletes file with given filename.
     *
     * @param   filename    name of file to delete
     */
    @Operation(summary = "Deletes file with given filename")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "File deleted"),
        @ApiResponse(responseCode = "403", description = "Not authorized", content = @Content),
        @ApiResponse(responseCode = "404", description = "File not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @DeleteMapping("/{filename}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(
            @Parameter(description = "Name of file to delete")
            @PathVariable("filename") String filename) {

        fileUploadService.delete(filename);
    }
}
