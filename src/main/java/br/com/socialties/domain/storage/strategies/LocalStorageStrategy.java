package br.com.socialties.domain.storage.strategies;

import br.com.socialties.domain.storage.StorageProperties;
import br.com.socialties.domain.storage.StorageService;
import br.com.socialties.domain.storage.exceptions.StorageException;
import br.com.socialties.domain.storage.exceptions.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class LocalStorageStrategy implements StorageService {

    private final Path rootLocation;

    @Autowired
    public LocalStorageStrategy(StorageProperties properties) throws StorageException {
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        init();
    }

    public String store(MultipartFile file) {
        var newFilename = generateFileName(file.getOriginalFilename());

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(Objects.requireNonNull(newFilename)))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            return newFilename;
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Resource retrieve(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public void delete(String filename) {
        Path file = load(filename);
        FileSystemUtils.deleteRecursively(file.toFile());
    }

    private void init() {
        if (Files.exists(rootLocation)) {
            return;
        }

        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    private Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    private String generateFileName(String filename) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        var now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toString();
        var string = now + "-" + UUID.randomUUID() + "-" + filename;
        var suffix = string.substring(string.lastIndexOf("."));

        md.update(string.getBytes(), 0, string.length());
        var md5Hash = new BigInteger(1, md.digest()).toString(16);

        return md5Hash + suffix;
    }

}
