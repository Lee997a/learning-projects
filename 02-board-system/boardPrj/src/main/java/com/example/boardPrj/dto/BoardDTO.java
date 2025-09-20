package com.example.boardPrj.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {

    private Long id;        // 게시판 고유 번호
    private String title;   // 제목
    private String content; // 내용
    private String writer;  // 작성자
    private LocalDateTime createdAt;    // 작성일
    private Integer likeCount;

    public BoardDTO(){
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

}
