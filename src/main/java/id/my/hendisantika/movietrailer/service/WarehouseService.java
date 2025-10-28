package id.my.hendisantika.movietrailer.service;

import id.my.hendisantika.movietrailer.exception.FileNotFoundException;
import id.my.hendisantika.movietrailer.exception.WarehouseException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.14
 * To change this template use File | Settings | File Templates.
 */
@Slf4j
@Service
public class WarehouseService {
    @Value("${storage.location}")
    private String storageLocation;

    //This is used to indicate that this method is going to be executed every time it finds a new instance of this
    // class.
    @PostConstruct
    public void startStoreFiles() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException exception) {
            throw new WarehouseException("Failed to initialize the location in the file store");
        }
    }

    public String storeFile(MultipartFile archive) {
        String filename = archive.getOriginalFilename();
        if (archive.isEmpty()) {
            throw new WarehouseException("Can't store an empty file");
        }
        try {
            InputStream inputStream = archive.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new WarehouseException("Error al almacenar el archive " + filename, exception);
        }
        return filename;
    }

    public Path fileUpload(String filename) {
        return Paths.get(storageLocation).resolve(filename);
    }

    public Resource uploadAsResource(String filename) {
        try {
            Path archive = fileUpload(filename);
            Resource resource = new UrlResource(archive.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("The file could not be found " + filename);
            }

        } catch (MalformedURLException exception) {
            throw new FileNotFoundException("The file could not be found " + filename, exception);
        }
    }

    public void deleteArchive(String filename) {
        Path archive = fileUpload(filename);
        try {
            FileSystemUtils.deleteRecursively(archive);
        } catch (Exception exception) {
            log.info("Error: ", exception);
        }
    }
}
