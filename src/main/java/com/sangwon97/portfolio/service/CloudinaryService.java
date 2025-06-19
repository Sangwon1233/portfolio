package com.sangwon97.portfolio.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadImage(MultipartFile file, String folderName);
}
