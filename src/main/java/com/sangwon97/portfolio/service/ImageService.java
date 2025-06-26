package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadAndSaveImage(MultipartFile file, String folderName);
    public String uploadBase64Image(String base64Data);
}
