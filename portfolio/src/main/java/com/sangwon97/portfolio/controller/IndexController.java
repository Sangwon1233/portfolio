package com.sangwon97.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sangwon97.portfolio.service.BoardService;
import com.sangwon97.portfolio.service.VisitorService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    
    private final BoardService boardService;
    private final VisitorService visitorService;

    public IndexController(BoardService boardService , VisitorService visitorService) {
        this.boardService = boardService;
        this.visitorService = visitorService;
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        String ip = request.getRemoteAddr();
        visitorService.logVisit(ip);

        model.addAttribute("todayCount", visitorService.countTodayVisits());
        model.addAttribute("recentProjects", boardService.getBoardsByType("project").stream().limit(5).toList());
        model.addAttribute("recentNotions", boardService.getBoardsByType("notion").stream().limit(5).toList());
        return "index";
    }
}
