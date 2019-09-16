package com.example.hellosustechbackend.filestorage;

import com.example.hellosustechbackend.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.stream.Stream;

@Service
@RestController
public class FileStorageImpl implements FileStorage {

    private Path rootLocation;

    public FileStorageImpl() {
        try {
            String path = "./src/main/java/com/example/hellosustechbackend/filelocation.properties";
            InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(path)));

            Properties properties = new Properties();
            properties.load(inputStream);
            rootLocation = Paths.get(properties.getProperty("Path"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

//        String uploadPicPath = "C:\\Users\\Huang Xu\\OneDrive\\Software Engineering\\github-classroom\\main-project-repository-hello-sustech\\back-end\\hello-sustech_back-end\\filestorage\\";
        try {
            try (InputStream inputStream = file.getInputStream()) {
                Path path = Paths.get(rootLocation + "/" + filename);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    @Override
    public Stream<Path> loadFiles() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to read stored file");
        }
    }
}
