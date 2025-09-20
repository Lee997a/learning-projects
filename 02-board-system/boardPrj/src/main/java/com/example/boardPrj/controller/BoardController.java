package com.example.boardPrj.controller;

import com.example.boardPrj.dto.BoardDTO;
import com.example.boardPrj.service.BoardLikeService;
import com.example.boardPrj.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final BoardLikeService boardLikeService;

    // 게시글 목록 페이지
    @GetMapping
    public String boardList(Model model) {
        List<BoardDTO> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "board/list";
    }

    // 게시글 상세 페이지
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        BoardDTO board = boardService.getBoardById(id);
        String userIp = getClientIp(request);
        boolean isLiked = boardLikeService.isLikedByUser(id, userIp);

        model.addAttribute("board", board);
        model.addAttribute("isLiked", isLiked);
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

//    // 좋아요 토글 (AJAX용)
//    @PostMapping("/{id}/like")
//    @ResponseBody
//    public Map<String, Object> toggleLike(@PathVariable Long id, HttpServletRequest request){
//        String userIp = getClientIp(request);
//        boolean isLiked = boardLikeService.isLikedByUser(id, userIp);
//        int likeCount = boardLikeService.getLikeCount(id);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("isLiked", isLiked);
//        result.put("likecount", likeCount);
//
//        return result;
//    }


    @PostMapping("/{id}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long id, HttpServletRequest request) {
        try {
            log.info("좋아요 요청 - boardId: {}", id);

            String userIp = getClientIp(request);
            log.info("사용자 IP: {}", userIp);

            // 이 부분이 중요! toggleLike 메서드를 호출해야 함
            boolean isLiked = boardLikeService.toggleLike(id, userIp);
            int likeCount = boardLikeService.getLikeCount(id);

            log.info("좋아요 결과 - isLiked: {}, likeCount: {}", isLiked, likeCount);

            Map<String, Object> result = new HashMap<>();
            result.put("isLiked", isLiked);
            result.put("likeCount", likeCount);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("좋아요 처리 중 오류 발생", e);

            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", true);
            errorResult.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }

    // 클라이언트 IP 주소 추출
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty() && !"unknown".equalsIgnoreCase(xfHeader)) {
            return xfHeader.split(",")[0].trim();
        }

        String xrHeader = request.getHeader("X-Real-IP");
        if (xrHeader != null && !xrHeader.isEmpty() && !"unknown".equalsIgnoreCase(xrHeader)) {
            return xrHeader;
        }

        String remoteAddr = request.getRemoteAddr();
        // IPv6 로컬주소를 IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
            return "127.0.0.1";
        }

        return remoteAddr;
    }
}