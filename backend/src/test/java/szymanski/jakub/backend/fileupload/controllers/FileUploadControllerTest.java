package szymanski.jakub.backend.fileupload.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.ResponseEntity;
import szymanski.jakub.backend.fileupload.services.FileUploadService;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class FileUploadControllerTest {

    private FileUploadService fileUploadService;
    private FileUploadController controller;

    @BeforeEach
    void setUp() {
        fileUploadService = mock(FileUploadService.class);
        controller = new FileUploadController(fileUploadService);
    }

    @Test
    void returnsRequestedFileAsResource() {
        Resource resource = new ByteArrayResource(new byte[]{1, 2, 3});
        when(fileUploadService.loadAsResource("photo.png")).thenReturn(resource);

        ResponseEntity<Resource> response = controller.getFile("photo.png");

        assertSame(resource, response.getBody());
        verify(fileUploadService).loadAsResource("photo.png");
    }

    @Test
    void delegatesUploadAndDeleteRequests() {
        MockMultipartFile file = new MockMultipartFile("file", "photo.png", "image/png", new byte[]{1});

        controller.uploadFile(file, 12L);
        controller.deleteFile("photo.png");

        verify(fileUploadService).upload(file, 12L);
        verify(fileUploadService).delete("photo.png");
    }
}
