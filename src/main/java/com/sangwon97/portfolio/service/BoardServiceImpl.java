package com.sangwon97.portfolio.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.repository.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public void save(Board board) {
        if (board.getCreatedAt() == null) {
            board.setCreatedAt(LocalDateTime.now());
        }
        board.setUpdatedAt(LocalDateTime.now());
        boardRepository.save(board);
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
