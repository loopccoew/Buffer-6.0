package com.phonepe.controller;

import com.phonepe.model.Upload;
import com.phonepe.service.UploadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uploads")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestBody Upload upload) {
        uploadService.saveUpload(upload);
        return "File uploaded!";
    }

    @GetMapping("/user/{userId}")
    public List<Upload> getUserUploads(@PathVariable String userId) {
        return uploadService.getUploadsByUser(userId);
    }
}
