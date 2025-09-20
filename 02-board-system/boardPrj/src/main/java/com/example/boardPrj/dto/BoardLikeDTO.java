package com.example.boardPrj.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardLikeDTO {

    private Long id;
    private Long boardId;
    private String userIp;
    private LocalDateTime createAt;

    public BoardLikeDTO(){};

    public BoardLikeDTO(Long boardId, String userIp){
        this.boardId = boardId;
        this.userIp = userIp;
    }
}
