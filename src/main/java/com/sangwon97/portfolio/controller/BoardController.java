package com.sangwon97.portfolio.controller;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.service.BoardService;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String list(@RequestParam String type,
                    @RequestParam(required = false) String subCategory,
                    Model model) {
        List<Board> boards = boardService.getBoards(type, subCategory);

        // ✅ 사용자에게 보여줄 이름 설정
        String displayName = switch (type) {
            case "notion" -> "메모";
            case "project" -> "프로젝트";
            case "company-issue" -> "회사 이슈";
            case "ideas" -> "아이디어";
            default -> type;
        };

        model.addAttribute("boards", boards);
        model.addAttribute("boardType", type);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("boardTypeDisplay", displayName);
        return "board/list";
    }

    // 게시글 상세
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, String type, Model model) {
        model.addAttribute("board", boardService.getBoard(id));

        String displayName = switch (type) {
            case "notion" -> "메모";
            case "project" -> "프로젝트";
            case "company-issue" -> "회사 이슈";
            case "ideas" -> "아이디어";
            default -> type;
        };
        model.addAttribute("boardTypeDisplay", displayName);
        return "board/view";
    }

    // 글쓰기 폼
    @GetMapping("/write")
    public String writeForm(@RequestParam String type, Model model) {
        model.addAttribute("boardType", type);

        String displayName = switch (type) {
        case "notion" -> "메모";
        case "project" -> "프로젝트";
        case "company-issue" -> "회사 이슈";
        case "ideas" -> "아이디어";
        default -> type;
    };
    model.addAttribute("boardTypeDisplay", displayName);
        return "board/write";
    }

    // 글 등록 처리
    @PostMapping("/write")
    public String write(@ModelAttribute Board board) {
    // 로그인 사용자 ID 추출
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName(); // DB 아이디 username으로 바꾸기

    // 작성자 직접 설정
    board.setAuthor(username);

    boardService.save(board);
    return "redirect:/board/list?type=" + board.getBoardType();
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam String type) {
        boardService.delete(id);
        return "redirect:/board/list?type=" + type;
    }
    //페이지 맵핑
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/modify"; // board/modify.html
    }
    //페이지 처리
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @ModelAttribute Board updatedBoard) {
        boardService.update(id, updatedBoard);
        return "redirect:/board/view/" + id + "?type=" + updatedBoard.getBoardType();
    }
   
}
