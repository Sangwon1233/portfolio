package com.sangwon97.portfolio.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file, String folderName);
    Map<?, ?> uploadBase64Image(String base64);
}
