package com.sangwon97.portfolio.controller;

import com.sangwon97.portfolio.domain.entity.Board;
import com.sangwon97.portfolio.service.BoardService;
import com.sangwon97.portfolio.util.AutoLinkUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.security.access.AccessDeniedException;
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

    Board board = boardService.getBoard(id);
    //test log
    System.out.println("내용 확인: [" + board.getContent() + "]");
    model.addAttribute("board", board);

    // type이 null일 경우 board에서 직접 꺼내도록 수정
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

    // 글쓰기 폼
    @PostMapping("/write")
    public String write(@ModelAttribute Board board, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        board.setAuthor(username);

        // 🔥 AutoLink + Sanitize 적용
        String rawContent = request.getParameter("content");
        String linkedContent = AutoLinkUtil.convertUrlsToLinks(rawContent);
        String cleanContent = Jsoup.clean(linkedContent, Safelist.relaxed());
        board.setContent(cleanContent);

        boardService.save(board);
        return "redirect:/board/list?type=" + board.getBoardType();
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, @RequestParam String type) {
        boardService.delete(id);
        return "redirect:/board/list?type=" + type;
    }

    // 수정폼
    @GetMapping("/modify/{id}")
    public String showModifyForm(@PathVariable Long id, Model model) {
        Board board = boardService.getBoard(id);
        model.addAttribute("board", board);
        return "board/modify";
    }

    // 수정 처리 (🔥 수정)
    @PostMapping("/modify/{id}")
    public String modifySubmit(@PathVariable Long id, @ModelAttribute Board board, Principal principal, HttpServletRequest request) {
        Board original = boardService.getBoard(id);

        if (!original.getAuthor().equals(principal.getName())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        original.setTitle(board.getTitle());
        original.setSubCategory(board.getSubCategory());
        original.setUpdatedAt(LocalDateTime.now());

        // 🔥 수정에서도 AutoLink + Sanitize 적용
        String rawContent = request.getParameter("content");
        String linkedContent = AutoLinkUtil.convertUrlsToLinks(rawContent);
        String cleanContent = Jsoup.clean(linkedContent, Safelist.relaxed());
        original.setContent(cleanContent);

        boardService.save(original);
        return "redirect:/board/view/" + id;
    }
}
