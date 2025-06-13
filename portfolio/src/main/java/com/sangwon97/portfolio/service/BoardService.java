package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getBoardsByType(String boardType) {
        return boardRepository.findByBoardTypeOrderByCreatedAtDesc(boardType);
    }

    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
