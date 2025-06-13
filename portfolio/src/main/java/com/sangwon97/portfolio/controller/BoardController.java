package com.sangwon97.portfolio.controller;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시판 목록
    @GetMapping("/list")
    public String list(@RequestParam String type, Model model) {
        model.addAttribute("boards", boardService.getBoardsByType(type));
        model.addAttribute("boardType", type);
        return "board/list";
    }

    // 게시글 상세
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.getBoard(id));
        return "board/view";
    }

    // 글쓰기 폼
    @GetMapping("/write")
    public String writeForm(@RequestParam String type, Model model) {
        model.addAttribute("boardType", type);
        return "board/write";
    }

    // 글 등록 처리
    @PostMapping("/write")
    public String write(@ModelAttribute Board board) {
        boardService.save(board);
        return "redirect:/board/list?type=" + board.getBoardType();
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam String type) {
        boardService.delete(id);
        return "redirect:/board/list?type=" + type;
    }
}
