package com.sangwon97.portfolio.service;

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
        boardRepository.save(board);
    }

    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow();
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
    
}
