package com.sangwon97.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

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

    public BoardServiceImpl(BoardRepository boardRepository, CloudinaryService cloudinaryService, ImageRepository imageRepository) {
        this.boardRepository = boardRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override //오버로딩
    public void save(Board board) {
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
            for (MultipartFile file : files) {.
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

    // @Override
    // public Board update(Long id, Board updatedBoard) {
    //     Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글 없음"));
    //     board.setTitle(updatedBoard.getTitle());
    //     board.setContent(updatedBoard.getContent());
    //     board.setUpdatedAt(LocalDateTime.now());
    //     return boardRepository.save(board); // ✅ 저장 후 리턴
    // }

    
}
