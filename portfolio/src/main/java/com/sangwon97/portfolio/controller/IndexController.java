package com.sangwon97.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sangwon97.portfolio.service.BoardService;

@Controller
public class IndexController {
    
    private final BoardService boardService;

    public IndexController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentProjects", boardService.getBoardsByType("project").stream().limit(5).toList());
        model.addAttribute("recentNotions", boardService.getBoardsByType("notion").stream().limit(5).toList());
        return "index"; // templates/index.html
    }
}
