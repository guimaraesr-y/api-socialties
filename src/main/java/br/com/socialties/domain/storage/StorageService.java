package br.com.socialties.domain.storage;

import br.com.socialties.domain.storage.exceptions.StorageException;
import br.com.socialties.domain.storage.exceptions.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface for file storage operations.
 */
public interface StorageService {

    /**
     * Stores a multipart file.
     *
     * @param file file to be stored
     * @return generated filename for the stored file
     * @throws StorageException if an error occurs while storing
     */
    String store(MultipartFile file) throws StorageException; // TODO: Return full path to resource instead

    /**
     * Loads a file as a Spring resource.
     *
     * @param filename filename
     * @return resource representing the file
     * @throws StorageFileNotFoundException if the file is not found or cannot be read
     */
    Resource retrieve(String filename) throws StorageFileNotFoundException;

    /**
     * Deletes a specific file from the repository.
     *
     * @param filename filename of the file to be deleted
     */
    void delete(String filename);

}
