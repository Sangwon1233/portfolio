package com.sangwon97.portfolio.service;

import com.sangwon97.portfolio.domain.dto.BoardModifyForm;
import com.sangwon97.portfolio.domain.entity.Board;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    void save(Board board); //단순저장 오버로딩
    void save(Board board, List<MultipartFile> files,String folderName);
    void delete(Long id);
    Board getBoard(Long id);
    List<Board> getBoards(String type);
    List<Board> getBoards(String type, String subCategory);
    void update(Board board, BoardModifyForm form, List<MultipartFile> files, List<Long> deleteImageIds, String folderName);
    // Board update(Long id, Board updatedBoard);
    

}
