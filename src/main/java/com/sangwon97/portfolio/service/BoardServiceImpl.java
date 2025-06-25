package com.sangwon97.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.domain.entity.Image;
import com.sangwon97.portfolio.repository.BoardRepository;
import com.sangwon97.portfolio.repository.ImageRepository;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public BoardServiceImpl(BoardRepository boardRepository, CloudinaryService cloudinaryService, ImageRepository imageRepository) {
        this.boardRepository = boardRepository;
        this.cloudinaryService = cloudinaryService;
        this.imageRepository = imageRepository;
    }

    @Override
    @Transactional
    public void save(Board board, List<MultipartFile> files, String folderName) {

        if (board.getCreatedAt() == null) {
            board.setCreatedAt(LocalDateTime.now());
        }
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);  // Board 먼저 저장해서 PK 얻음

        // 이미지들 처리
        for (MultipartFile file : files) {
            String uploadedUrl = cloudinaryService.uploadImage(file, folderName);

            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setImageUrl(uploadedUrl);
            image.setBoard(board);  // Board 연결이 핵심
            imageRepository.save(image);
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

    @Override
    public Board update(Long id, Board updatedBoard) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new RuntimeException("게시글 없음"));
        board.setTitle(updatedBoard.getTitle());
        board.setContent(updatedBoard.getContent());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board); // ✅ 저장 후 리턴
    }

    
}
