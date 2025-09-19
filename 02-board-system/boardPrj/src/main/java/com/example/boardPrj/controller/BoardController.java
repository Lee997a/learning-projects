package com.example.boardPrj.controller;

import com.example.boardPrj.dto.BoardDTO;
import com.example.boardPrj.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 페이지
    @GetMapping
    public String boardList(Model model) {
        List<BoardDTO> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/detail";
    }

    // 게시글 작성 페이지
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new BoardDTO());
        return "board/write";
    }

    // 게시글 작성 처리
    @PostMapping("/write")
    public String writeBoard(@ModelAttribute BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        boardService.createBoard(boardDTO);
        redirectAttributes.addFlashAttribute("message", "게시글이 작성되었습니다.");
        return "redirect:/board";
    }

    // 게시글 수정 페이지
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        BoardDTO board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/edit";
    }

    // 게시글 수정 처리
    @PostMapping("/{id}/edit")
    public String editBoard(@PathVariable Long id, @ModelAttribute BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        boardDTO.setId(id);
        boardService.updateBoard(boardDTO);
        redirectAttributes.addFlashAttribute("message", "게시글이 수정되었습니다.");
        return "redirect:/board/" + id;
    }

    // 게시글 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteBoard(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boardService.deleteBoard(id);
        redirectAttributes.addFlashAttribute("message", "게시글이 삭제되었습니다.");
        return "redirect:/board";
    }
}