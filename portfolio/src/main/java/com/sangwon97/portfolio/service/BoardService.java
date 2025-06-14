package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Board;

import java.util.List;

public interface BoardService {
    void save(Board board);
    void delete(Long id);
    Board getBoard(Long id);
    List<Board> getBoards(String type);
    List<Board> getBoards(String type, String subCategory);

}
