package com.example.hellosustechbackend.web;

import com.example.hellosustechbackend.filestorage.FileStorage;
import com.example.hellosustechbackend.service.UploadService;
import com.example.hellosustechbackend.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFileAPI {

    @Autowired
    FileStorage fileStorage;

    @Autowired
    UploadService uploadService;

//    @GetMapping("/")
//    public String index() {
//        System.out.println("get request");
//        return "multipartfile/uploadform.html";
//    }

    /**
     *
     * @param file
     * @param model
     * @return
     */
    @PostMapping("/")
    public Status uploadMultipartFile(@RequestParam("uploadfile") MultipartFile file, Model model) {
        Status status = new Status();
        try {
            fileStorage.store(file);
            System.out.println("Upload successful");
            model.addAttribute("message", "File uploaded successfully! -> filename = " + file.getOriginalFilename());
            String command = "python3 ../../TrainingModel/SCRIPT/script.py ./image/"+file.getOriginalFilename();
            System.err.println(command);
            String result = uploadService.readConsole(command);
            command = "rm ./image/"+file.getOriginalFilename();
            
            status.setResult(true);
            status.setDescription(result);
        } catch (Exception e) {
            System.out.println("Upload fail");
//            model.addAttribute("message", "Fail! -> uploaded filename: " + file.getOriginalFilename());
            status.setDescription("Upload fail");
        }
        return status;
    }
}
