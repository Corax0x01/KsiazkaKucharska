package szymanski.jakub.backend.fileupload.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import szymanski.jakub.backend.config.FileUploadProperties;
import szymanski.jakub.backend.fileupload.exceptions.FileUploadException;
import szymanski.jakub.backend.fileupload.exceptions.UploadedFileNotFoundException;
import szymanski.jakub.backend.recipe.dtos.RecipeDto;
import szymanski.jakub.backend.recipe.services.RecipeService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class FileUploadServiceImplTest {

    @TempDir
    Path tempDir;

    private RecipeService recipeService;
    private FileUploadServiceImpl fileUploadService;

    @BeforeEach
    void setUp() {
        recipeService = mock(RecipeService.class);
        FileUploadProperties properties = new FileUploadProperties();
        properties.setLocation(tempDir.toString());
        fileUploadService = new FileUploadServiceImpl(properties, recipeService);
        fileUploadService.init();
    }

    @Test
    void uploadsWithGeneratedFilenameAndUpdatesRecipeImage() throws Exception {
        when(recipeService.exists(13L)).thenReturn(true);
        MockMultipartFile file = new MockMultipartFile(
                "file", "meal photo.png", "image/png", "image-bytes".getBytes()
        );

        fileUploadService.upload(file, 13L);

        verify(recipeService).partialUpdate(eq(13L), argThat(recipe ->
                recipe.getImageName().matches("[0-9a-fA-F-]{36}\\.png")));
        try (var files = Files.list(tempDir)) {
            Path savedFile = files.findFirst().orElseThrow();
            assertTrue(savedFile.getFileName().toString().matches("[0-9a-fA-F-]{36}\\.png"));
            assertArrayEquals("image-bytes".getBytes(), Files.readAllBytes(savedFile));
        }
    }

    @Test
    void rejectsEmptyFilesMissingRecipesAndPathsOutsideUploadDirectory() {
        when(recipeService.exists(13L)).thenReturn(true);
        assertThrows(FileUploadException.class, () -> fileUploadService.upload(
                new MockMultipartFile("file", "empty.png", "image/png", new byte[0]), 13L));

        when(recipeService.exists(14L)).thenReturn(false);
        assertThrows(FileUploadException.class, () -> fileUploadService.upload(
                new MockMultipartFile("file", "photo.png", "image/png", new byte[]{1}), 14L));

        assertThrows(FileUploadException.class, () -> fileUploadService.upload(
                new MockMultipartFile("file", "../outside.png", "image/png", new byte[]{1}), 13L));
        verify(recipeService, never()).partialUpdate(eq(14L), any(RecipeDto.class));
    }

    @Test
    void loadsReadableResourcesAndReportsMissingFiles() throws Exception {
        Path saved = tempDir.resolve("available.jpg");
        Files.writeString(saved, "photo");

        Resource resource = fileUploadService.loadAsResource("available.jpg");

        assertTrue(resource.exists());
        assertEquals("photo", Files.readString(resource.getFile().toPath()));
        assertThrows(UploadedFileNotFoundException.class,
                () -> fileUploadService.loadAsResource("missing.jpg"));
    }

    @Test
    void deletesOneFileOrTheWholeUploadDirectory() throws Exception {
        Path one = tempDir.resolve("one.png");
        Path two = tempDir.resolve("two.png");
        Files.writeString(one, "one");
        Files.writeString(two, "two");

        fileUploadService.delete("one.png");

        assertFalse(Files.exists(one));
        assertTrue(Files.exists(two));
        fileUploadService.deleteAll();
        assertFalse(Files.exists(tempDir));
    }

    @Test
    void rejectsBlankUploadLocation() {
        FileUploadProperties properties = new FileUploadProperties();
        properties.setLocation("  ");

        assertThrows(FileUploadException.class, () -> new FileUploadServiceImpl(properties, recipeService));
    }
}
