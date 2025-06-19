package com.sangwon97.portfolio.controller;

import com.sangwon97.portfolio.domain.entity.Image;
import com.sangwon97.portfolio.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("folder") String folder) {
        Image savedImage = imageService.uploadAndSaveImage(file, folder);
        return ResponseEntity.ok(savedImage);
    }
}
