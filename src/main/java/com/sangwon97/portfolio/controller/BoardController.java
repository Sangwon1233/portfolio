package com.sangwon97.portfolio.controller;

import com.sangwon97.portfolio.domain.dto.BoardModifyForm;
import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.service.BoardService;

import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);

        if (type == null) {
            type = board.getBoardType();
        }
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

    // 글쓰기 폼 GET
    @GetMapping("/write")
    public String showWriteForm(@RequestParam String type, Model model) {
        model.addAttribute("boardType", type);
        return "board/write";
    }

    // 글쓰기 POST
    @PostMapping("/write")
    public String write(@ModelAttribute Board board,@RequestParam("files") List<MultipartFile> files, 
        Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        board.setAuthor(username);


        boardService.save(board, files, "board");
        return "redirect:/board/list?type=" + board.getBoardType();
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam String type) {
        boardService.delete(id);
        return "redirect:/board/list?type=" + type;
    }

    // 수정 폼
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        model.addAttribute("boardType", board.getBoardType()); 
        return "board/modify";
    }

    // 수정 처리
    @PostMapping("/modify/{id}")
    public String modifySubmit(@PathVariable Long id,
                                @ModelAttribute BoardModifyForm form,
                                @RequestParam(value = "files", required = false) List<MultipartFile> files,
                                @RequestParam(value = "deleteImageIds", required = false) List<Long> deleteImageIds,
                                Principal principal) {

        Board original = boardService.getBoard(id);

        if (!original.getAuthor().equals(principal.getName())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        boardService.update(original, form, files, deleteImageIds, "board");
        return "redirect:/board/view/" + id;
    }
}
