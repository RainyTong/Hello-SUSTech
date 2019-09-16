package com.example.hellosustechbackend.filestorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorage {
    void store(MultipartFile file);

    Resource loadFile(String filename);

    void deleteAll();

    void init();

    Stream<Path> loadFiles();
}
