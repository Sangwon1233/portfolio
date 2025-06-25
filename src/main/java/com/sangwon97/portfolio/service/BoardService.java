package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.entity.Board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    void save(Board board, List<MultipartFile> files,String folderName);
    void delete(Long id);
    Board getBoard(Long id);
    List<Board> getBoards(String type);
    List<Board> getBoards(String type, String subCategory);
    Board update(Long id, Board updatedBoard);

    

}
