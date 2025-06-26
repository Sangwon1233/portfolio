package com.sangwon97.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sangwon97.portfolio.domain.dto.BoardModifyForm;
import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.domain.entity.Image;
import com.sangwon97.portfolio.repository.BoardRepository;
import com.sangwon97.portfolio.repository.ImageRepository;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;
    private final ImageServiceImpl imageServiceImpl;

    public BoardServiceImpl(BoardRepository boardRepository, CloudinaryService cloudinaryService, ImageRepository imageRepository,ImageServiceImpl imageServiceImpl) {
        this.boardRepository = boardRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
        this.imageServiceImpl = imageServiceImpl;
    }

    @Override //오버로딩
    public void save(Board board) {
        String replacedContent = replaceBase64Images(board.getContent(), board);
        board.setContent(replacedContent);
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);
    }

     @Override
    public void save(Board board, List<MultipartFile> files, String folderName) {
        boardRepository.save(board);

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 빈 파일은 skip
                }
                    String uploadedUrl = cloudinaryService.uploadImage(file, folderName);
                    Image image = new Image();
                    image.setFileName(file.getOriginalFilename());
                    image.setImageUrl(uploadedUrl);
                    image.setBoard(board);
                    imageRepository.save(image);
            }
        }
    }

    @Override
    public void update(Board board, BoardModifyForm form, List<MultipartFile> files, List<Long> deleteImageIds, String folderName) {
        board.setTitle(form.getTitle());
        board.setContent(form.getContent());
        board.setSubCategory(form.getSubCategory());
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);

        if (deleteImageIds != null) {
            for (Long imageId : deleteImageIds) {
                imageRepository.deleteById(imageId);
            }
        }

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue; // 빈 파일은 skip
                }
                String uploadedUrl = cloudinaryService.uploadImage(file, folderName);
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setImageUrl(uploadedUrl);
                image.setBoard(board);
                imageRepository.save(image);
            }
        }
    }


    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Board getBoard(Long id) {
        return boardRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. ID: " + id));
    }

    @Override
    public List<Board> getBoards(String type) {
        return boardRepository.findByBoardTypeOrderByCreatedAtDesc(type);
    }

    @Override
    public List<Board> getBoards(String type, String subCategory) {
        if ("notion".equals(type) && subCategory != null && !subCategory.isBlank()) {
            return boardRepository.findByBoardTypeAndSubCategoryOrderByCreatedAtDesc(type, subCategory);
        }
        return getBoards(type);
    }

    private String replaceBase64Images(String content, Board board) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (Element img : images) {
            String src = img.attr("src");
            if (src != null && src.startsWith("data:image")) {
                // base64 업로드
                String url = imageServiceImpl.uploadBase64Image(src);

                // DB 저장
                Image image = new Image();
                image.setImageUrl(url);
                image.setBoard(board);
                imageRepository.save(image);

                // HTML 교체
                img.attr("src", url);
            }
        }

        return doc.body().html();
    }


    
}
