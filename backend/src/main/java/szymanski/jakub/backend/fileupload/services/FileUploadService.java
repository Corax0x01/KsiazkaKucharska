package szymanski.jakub.backend.fileupload.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import szymanski.jakub.backend.fileupload.exceptions.FileUploadException;
import szymanski.jakub.backend.fileupload.exceptions.UploadedFileNotFoundException;

import java.nio.file.Path;

public interface FileUploadService {

    /**
     * Initializes file uploads.
     *
     * @throws FileUploadException  if error with uploading file occurs
     */
    void init();

    /**
     * Uploads file.
     *
     * @param   file                {@link MultipartFile file} to upload
     * @param   recipeId            ID of recipe that file is attached to
     * @throws  FileUploadException if error with uploading file occurs
     */
    void upload(MultipartFile file, Long recipeId);

    /**
     * Loads file by filename.
     *
     * @param   filename    name of file to load
     * @return              {@link Path} of file with given filename.
     */
    Path load(String filename);

    /**
     * Loads file by filename as resource.
     *
     * @param   filename            name of file to load
     * @return                      loaded file as {@link Resource} object
     * @throws  FileUploadException if error with uploading file occurs
     * @throws  UploadedFileNotFoundException   if file was not found
     */
    Resource loadAsResource(String filename);

    /**
     * Deletes all files.
     */
    void deleteAll();

    /**
     * Deletes file with given filename.
     *
     * @param   filename    name of file to delete
     * @throws  FileUploadException if file was not found
     */
    void delete(String filename);
}
