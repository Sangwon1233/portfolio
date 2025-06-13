package com.sangwon97.portfolio.repository;

import com.sangwon97.portfolio.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByBoardTypeOrderByCreatedAtDesc(String boardType);
}
