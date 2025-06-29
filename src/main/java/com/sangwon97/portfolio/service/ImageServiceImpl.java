package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Image;
import com.sangwon97.portfolio.repository.ImageRepository;
import com.sangwon97.portfolio.service.CloudinaryService;
import com.sangwon97.portfolio.service.ImageService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(CloudinaryService cloudinaryService, ImageRepository imageRepository) {
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override
    public Image uploadAndSaveImage(MultipartFile file, String folderName) {
        String uploadedUrl = cloudinaryService.uploadImage(file, folderName);
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setImageUrl(uploadedUrl);
        return imageRepository.save(image);
    }

    @Override
    public String uploadBase64Image(String base64Data) {
        try {
            Map<?, ?> result = cloudinaryService.uploadBase64Image(base64Data);
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Cloudinary 업로드 실패", e);
        }
}
}
