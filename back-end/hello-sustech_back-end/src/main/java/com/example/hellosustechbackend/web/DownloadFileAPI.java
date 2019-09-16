package com.example.hellosustechbackend.web;

import com.example.hellosustechbackend.filestorage.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadFileAPI {

    @Autowired
    FileStorage fileStorage;

//    /**
//     * Retrieve Files' Information
//     */
//    @GetMapping("/files")
//    public String getListFiles(Model model) {
//        List<FileInfo> fileInfos = fileStorage.loadFiles().map(
//                path -> {
//                    String filename = path.getFileName().toString();
//                    String url = MvcUriComponentsBuilder.fromMethodName(DownloadFileAPI.class,
//                            "downloadFile", path.getFileName().toString()).build().toString();
//                    return new FileInfo(filename, url);
//                }
//        )
//                .collect(Collectors.toList());
//
//        model.addAttribute("files", fileInfos);
//        return "multipartfile/listfiles.html";
//    }

    /**
     *  Download Files
     * @param filename the name of the file to be downloaded
     * @return
     */
    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = fileStorage.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(file);
    }
}
